package com.practice.edituserprofiledemo;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.practice.edituserprofiledemo.model.PImageData;
import com.practice.edituserprofiledemo.model.entity.ApiResultDto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

import static android.app.Activity.RESULT_OK;
import static com.practice.edituserprofiledemo.Config.KEY_IMAGE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements Button.OnClickListener {

    private static final String TAG = "ProfileFragment";
    private static final int MAX_WIDTH = 640;
    private static final int MAX_HEIGHT = 640;
    private ImageView profileView;
    private ImageButton imageButton;
    // private Bitmap cameraBitmap;

    String baseUrl = "http://www.ppizil.kro.kr/review/v1/file/";
    String thumbnailImagePath = "http://www.ppizil.kro.kr/review/files/1.jpeg";
    String originImagePath = "http://www.ppizil.kro.kr/review/files/0.jpeg";


    private static final int REQUEST_CAMERA = 101;
    private static final int PICK_IMAGE_REQUEST = 102;
    private static final int REQUEST_CODE_CROP_IMAGE = 103;


    String cameraPermissions[];
    String storagePermissions[];

    Uri image_uri;
    File photoFile;
    String imageUriRetrofit;

    private Bitmap bitmap;
    SharedPreferences sharedPreferences;

    public ProfileFragment() {
        // Required empty public constructor
        super();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        bindView(view);
        imageButton.setOnClickListener(this);
        //setData(KEY_IMAGE);
        setData(imageUriRetrofit);

        return view;
    }

    private void bindView(View view) {
        profileView = view.findViewById(R.id.profile_image);
        imageButton = view.findViewById(R.id.btn_choose_picture);
    }

    @Override
    public void onClick(View v) {
        selectImage();


        //updateData(image_uri);
        // updateData(photoFile);
    }

    private void setData(String internetUrl) {
        Glide.with(getActivity())
                .load(internetUrl)
                .thumbnail(0.25f)
                .apply(RequestOptions.circleCropTransform())
                .into(profileView);

    }

    private void selectImage() {
        checkPermission();
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(cameraIntent, REQUEST_CAMERA);

    }

    private void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }


    private void checkPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }



    public void fixOrientation() {
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
    }


    public void onResult(int requestCode, int resultCode, @Nullable Intent data) throws IOException {
        Log.e("aaa", requestCode + "," + resultCode);
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case PICK_IMAGE_REQUEST:
                    image_uri = data.getData();

                    Log.e("bbb", "onActivityResult: " + image_uri);
                    String uriPath = getRealPathFromURI(image_uri);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
                    if (bitmap.getWidth() > MAX_WIDTH || bitmap.getHeight() > MAX_HEIGHT) {
                       bitmap= resizeImage(bitmap);
                    }
                    fixOrientation();
                    profileView.setImageBitmap(bitmap);
                    updateData(uriPath);

                    break;

                case REQUEST_CAMERA:
                    bitmap = (Bitmap) data.getExtras().get("data");

                    fixOrientation();
                    String uriPathPicture = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "title", null);
                    image_uri = Uri.parse(uriPathPicture);
                    if (bitmap.getWidth() > MAX_WIDTH || bitmap.getHeight() > MAX_HEIGHT) {
                       bitmap =  resizeImage(bitmap);
                    }



                    compressImage(uriPathPicture);
                    String uriPathReal = getRealPathFromURI(image_uri);
                    updateData(uriPathReal);
                    break;
            }
        }
    }

    private void compressImage(String uriPath) {
        File file = new File(uriPath);
        try {
            bitmap = BitmapFactory.decodeFile(file.getPath());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        } catch (Throwable t) {
            Log.e("ERROR", "Error compressing file." + t.toString());
            t.printStackTrace();
        }
    }

    private Bitmap resizeImage(Bitmap bitmap) {
        int newWidth = (int) (bitmap.getWidth() * 0.8);
        int newHeight = (int) (bitmap.getWidth() * 0.8);
        Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedImage;
    }


    private String getRealPathFromURI(Uri contentUri) {
        //String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String[] filePathColumn = {MediaStore.Images.Thumbnails.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, filePathColumn, null, null, null);

        Cursor cursor = loader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);

        cursor.close();
        return result;
    }


//    private Bitmap getThumbnailPathForLocalFile(Activity context, Uri fileUri){
//        Long fileId = getRealPathFromURI(fileUri);
//        return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(),fileId,MediaStore.Images.Thumbnails.MICRO_KIND,null);
//    }


    private String convertToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }


    private void updateData(String uri) {
        imageUriRetrofit = uri;
        Log.e("connect0000", uri);
        File file = new File(uri);
        RequestBody originalFile = RequestBody.create(file, MediaType.parse("image/JPEG"));

        String getToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3QxMUB0ZXN0dGVzdHRlc3QuY29tIiwidWlkIjoxNTQsIm5pY2tuYW1lIjoiaGgiLCJpYXQiOjE1OTQzMDk5MTUsImV4cCI6MTU5NDM5NjMxNX0.D3Ov_dVrIlAr3I_dCBByXqfKOe13FnQU_nZfeBevQ3A";


        RetrofitInterface api = RetrofitClient.getRestMethods();
        // MultipartBody.Part originFileName = MultipartBody.Part.createFormData("0", file.getName(), requestFile);
        // MultipartBody.Part thumbnailFileName = MultipartBody.Part.createFormData("1", file.getName(), requestFile);
        // name : key값 , filename이 파일명
        MultipartBody.Part originFileName = MultipartBody.Part.createFormData("files", "0", originalFile);
        MultipartBody.Part thumbnailFileName = MultipartBody.Part.createFormData("files", "1", originalFile);

        // Log.e("connect00", "lll");
        Call<ApiResultDto> call = api.pimage(getToken, originFileName, thumbnailFileName);
        // Log.e("connect0", "aaaaa");
        call.enqueue(new Callback<ApiResultDto>() {
            @Override
            public void onResponse(Call<ApiResultDto> call, Response<ApiResultDto> response) {
                //     Log.e("connect1", "aaaa" + response);
                if (response.isSuccessful()) {
                    //       Log.e("connect1", "vvvv" + response);
                    ApiResultDto apiResultDto = response.body();
                    JsonObject resultData = apiResultDto.getResultData();

                    PImageData pImageData = new Gson().fromJson(resultData, PImageData.class);


                    String storedPath = pImageData.getStoredPath();
                    int index = storedPath.indexOf(".");
                    String thumbnailImage = baseUrl + storedPath.substring(0, index) + "Thumbnail";
                    String originalImage = baseUrl + storedPath;
                    //setData(thumbnailImage);
                    Log.i("thumbnailImage", thumbnailImage);
                    Log.i("originalImage", originalImage);


                    //  String imagePath = baseStoragePath+resultData.get("uri").getAsString();
                    // String img = response.body().getResultData().get("storedPath").getAsString();

                    setData(uri);


                    Toast.makeText(getActivity(), response.body().toString(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResultDto> call, Throwable t) {
                Log.d(TAG, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                //Logm 으로 하면 자동 Log가 입력이 된다. 이후 TAG 설정을 위해 Logt를 입력하여서 자동 완성 시킨다.
            }
        });


    }
}

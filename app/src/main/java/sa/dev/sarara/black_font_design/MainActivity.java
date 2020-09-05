package sa.dev.sarara.black_font_design;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1 ;
    Button FetchBtn, SaveBtn ;
    ImageView ImgHolder;
    Bitmap bitmap;
    InputStream inputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FetchBtn = findViewById(R.id.fetch_btn);
        SaveBtn = findViewById(R.id.save_btn);
        ImgHolder = findViewById(R.id.image_holder);

//      when button clicked
        FetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_image();
            }
        });

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap==null){
                    Toast.makeText(MainActivity.this,"please pick an image from pick button",Toast.LENGTH_LONG).show();
                }
                else{
                    saveImageBitmap(bitmap);
                }

            }
        });

    }


//    pick image uri/full path from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImgHolder.setImageBitmap(bitmap);
        }
    }


//   open a galleries
    public void pick_image(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select pic"), PICK_IMAGE);
        }


     public void filterImage(Uri img){
         Drawable drawable = Drawable.createFromPath(img.getPath());
//         drawable.setColorFilter();
//         String a = "sara";
     }

    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    public void saveImageBitmap(Bitmap image_bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();

        if (isStoragePermissionGranted()) { // check or ask permission
            File myDir = new File(root, "/BlackFontDesign");
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            String timeStamp = new SimpleDateFormat("yyMMdd-hhmmss").format(new Date());
            String fname =  "BlackFont"+ timeStamp + ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile(); // if file already exists will do nothing
                FileOutputStream out = new FileOutputStream(file);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(MainActivity.this,"please pick an image from pick button",Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"Error"+e,Toast.LENGTH_LONG).show();

            }

            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null);
        }
    }

//     TODO 1:  sava and retrive image using


//     TODO 3: ADD FILTERS FRAGMENT USING PhotoFiltersSDK LIBRARY

 //    TODO 4: UNDO/REDO OPTIONS






}

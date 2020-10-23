package sa.dev.sarara.black_font_design;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
//import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
//    id for pick image
    private static final int PICK_IMAGE = 1 ;
    Button FetchBtn, SaveBtn, FilterBtn ;
    ImageView ImgHolder, FilteredImg;
    Bitmap bitmap;
    Canvas canvas;
    BlendMode blendMode;
    InputStream inputStream;
    Bitmap outputImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        System.loadLibrary("NativeImageProcessor");
//      initializing views
        FetchBtn = findViewById(R.id.fetch_btn);
        SaveBtn = findViewById(R.id.save_btn);
        FilterBtn = findViewById(R.id.filter_btn);
        ImgHolder = findViewById(R.id.image_holder);
        FilteredImg = findViewById(R.id.img_after);





//      pick button clicked
        FetchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_image();
            }
        });
//      save button clicked
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              no image exist | so it won't save an empty pixels bitmap
                if(bitmap==null){
                    Toast.makeText(MainActivity.this,"please pick an image from pick button",Toast.LENGTH_LONG).show();
                }
                else{
                    saveImageBitmap(outputImage);

                }

            }
        });


        FilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilteredImg.setImageBitmap(FilterImage(bitmap));
            }
        });

    }


//    pick image uri/full path from gallery
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
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


    public boolean isStoragePermissionGranted() {
        String TAG = "Storage Permission";
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
            String timeStamp = new SimpleDateFormat("yyMMdd-hh-mm-ss").format(new Date());
            String fname =  "BFD"+timeStamp+ ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) {
                file.delete(); }
            try {
                Log.d("main_activity", "saveImageBitmap: "+"1");
                file.createNewFile(); // if file already exists will do nothing
                FileOutputStream out = new FileOutputStream(file);
                image_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                Toast.makeText(MainActivity.this,"image saved",Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this,"Error"+e,Toast.LENGTH_LONG).show();
                Log.d("main_activity", "saveImageBitmap: "+"not saved "+ e.getMessage());
            }
            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, new String[]{file.getName()}, null); }
    }


    public Bitmap filterImage(Bitmap img){

//        Drawable drawable = Drawable.createFromPath(img.getPath());
//         drawable.setColorFilter();
//         String a = "sara";
        canvas = new Canvas(img);
        blendMode = BlendMode.PLUS;
//        DrawFilter drawFilter = new DrawFilter();
//        drawFilter =
//        canvas.setDrawFilter();
        canvas.drawColor(12,blendMode);

        return img;
    }

    public Bitmap FilterImage(Bitmap bitmap){
        //TODO : FILTER IS GOOD IN MANY WAY : USING PAINT OR BLUEPAINT OR canvas.drawRect or drawcolor() or drawpaint()
        Paint paint = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        paint.setColorFilter(filter);
        outputImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(outputImage);

        Paint bluePaint = new Paint();
        bluePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        bluePaint.setShader(new BitmapShader(outputImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        bluePaint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY));

        canvas.setBitmap(outputImage);
        canvas.drawPaint(bluePaint);
        return outputImage;
    }

// todo 1000 ugh error from no where, there was nothing until today 24\9. saving file not working don't know why !!!!!!! shit
//    the code is all right it works last time but now ?

//     TODO 3: ADD FILTERS USING PhotoFiltersSDK LIBRARY

 //    TODO 4: UNDO/REDO OPTIONS

//    TODO 5 : CONTENT PROVIDER TO LOAD IMAGE
}


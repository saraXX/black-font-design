package sa.dev.sarara.black_font_design;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1 ;
    Button FIB ;
    ImageView img;
    Uri fullPhotoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FIB = findViewById(R.id.fitch_image_btn);
        img = findViewById(R.id.canvas);

        FIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick_image();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            fullPhotoUri = data.getData();
            img.setImageURI(fullPhotoUri);
        }
    }

    public void pick_image(){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"select pic"), PICK_IMAGE);
        }
     public void filterImage(Uri img){
         Drawable drawable = Drawable.createFromPath(img.getPath());
//         drawable.setColorFilter();
     }

}

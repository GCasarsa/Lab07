package lab08c2016.ar.edu.utn.frsf.isi.dam.ccv.reclamosonline;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Gabriel on 12/01/2017.
 */
public class AltaReclamoActivity extends AppCompatActivity {

    String imagen;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_reclamo);

        Button botonReclamo = (Button) findViewById(R.id.buttonReclamo);
        Button botonCancelar = (Button) findViewById(R.id.buttonCancelar);
        Button botonImagen = (Button) findViewById(R.id.buttonImagen);
        final EditText descripcion = (EditText) findViewById(R.id.editTextDescripcion);
        final EditText telefono = (EditText) findViewById(R.id.editTextTelefono);
        final EditText mail = (EditText) findViewById(R.id.editTextMail);
        imagen = null;

        botonReclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descripcion.getText().toString().isEmpty() || telefono.getText().toString().isEmpty() || mail.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Todos los campos son obligatorios.", Toast.LENGTH_LONG).show();
                }
                Bundle extras = getIntent().getExtras();
                LatLng ubicacion = (LatLng) extras.get("coordenadas");
                Reclamo reclamo = new Reclamo(descripcion.getText().toString(),telefono.getText().toString(),mail.getText().toString(), ubicacion.latitude, ubicacion.longitude, imagen);
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK,returnIntent);
                returnIntent.putExtra("result",reclamo);
                System.out.println("PASO: ANTES DEL FINISH");

                finish();
            }
        });
        botonImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(data != null){
                imagen = getRealPathFromURI(data.getData());
            }
        }

    }

    private String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}

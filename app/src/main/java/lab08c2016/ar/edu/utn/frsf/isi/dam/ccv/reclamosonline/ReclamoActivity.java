package lab08c2016.ar.edu.utn.frsf.isi.dam.ccv.reclamosonline;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class ReclamoActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap myMap;
    static final int CODIGO_RESULTADO_ALTA_RECLAMO = 1;
    private ArrayList<Reclamo> listaReclamos;
    private ArrayList<Polyline> listaLineas;
    private ImageButton eliminaRutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);

        eliminaRutas = (ImageButton) findViewById(R.id.buttonEliminarRutas);
        eliminaRutas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i < listaLineas.size(); i++){
                    listaLineas.get(i).remove();
                }
                listaLineas.clear();
            }
        });
        listaReclamos = new ArrayList<>();
        listaLineas = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(myMap!=null)
            onMapReady(myMap);
    }


    @Override
    public void onMapLongClick(LatLng punto) {
        Intent i = new Intent(ReclamoActivity.this, AltaReclamoActivity.class);
        i.putExtra("coordenadas", punto);
        System.out.println("PASO: ANTES DE LA OTRA");
        startActivityForResult(i, CODIGO_RESULTADO_ALTA_RECLAMO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent reclamo) {
        if (requestCode == CODIGO_RESULTADO_ALTA_RECLAMO) {
            if (resultCode == RESULT_OK) {
                Reclamo temp = (Reclamo) reclamo.getExtras().get("result");
                listaReclamos.add(temp);
            }
        }

    }

    @Override public void onMapReady(GoogleMap googleMap){
        myMap= googleMap;
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.setOnMapLongClickListener(this);
        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                final EditText cantidadKilometros = new EditText(ReclamoActivity.this);
                cantidadKilometros.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder= new AlertDialog.Builder(ReclamoActivity.this);
                builder.setMessage("Ingrese la cantidad de kilómetros:")
                        .setTitle("Buscar puntos cercanos")
                        .setView(cantidadKilometros)
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                if(!cantidadKilometros.getText().toString().isEmpty()){
                                    for(int j = 0; j < listaReclamos.size(); j++){
                                        float[] resultado = new float[1];
                                        android.location.Location.distanceBetween(marker.getPosition().latitude, marker.getPosition().longitude, listaReclamos.get(j).getLatitud(), listaReclamos.get(j).getLongitud(), resultado);
                                        System.out.println("resultado: " + resultado[0] + "   " + (resultado[0] <= Float.parseFloat(cantidadKilometros.getText().toString())));
                                        if(resultado[0] <= 1000* Float.parseFloat(cantidadKilometros.getText().toString())){
                                            Polyline line = myMap.addPolyline(new PolylineOptions()
                                                    .add(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude),
                                                            new LatLng(listaReclamos.get(j).getLatitud(), listaReclamos.get(j).getLongitud()))
                                                    .width(5)
                                                    .color(Color.RED));
                                            listaLineas.add(line);
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(ReclamoActivity.this,"Debe ingresar la cantidad de kilometros",Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(ReclamoActivity.this,"",Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        });
        for(int i = 0; i <listaReclamos.size(); i++){
            myMap.addMarker(new MarkerOptions()
                    .position(new LatLng(listaReclamos.get(i).getLatitud(), listaReclamos.get(i).getLongitud()))
                    .title("Reclamo de " + listaReclamos.get(i).getEmail())
                    .snippet(listaReclamos.get(i).getTitulo())
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
        actualizarMapa();
    }

    private void actualizarMapa() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},9999);
            myMap.setMyLocationEnabled(true);
            return;
        }
        myMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reclamo, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

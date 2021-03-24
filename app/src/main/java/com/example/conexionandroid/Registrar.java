package com.example.conexionandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;


public class Registrar extends Activity implements View.OnClickListener {
    private EditText email, pass,edad,direccion,nombre;
    private Button mRegister;
    CharSequence text = "Usuario creado!";
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String REGISTER_URL
            ="http://192.168.1.6/Conexion_android/registrar.php";

    private static final String TAG_SUCCESS = "Exito";
    private static final String TAG_MESSAGE = "Mensaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        email = (EditText)findViewById(R.id.etxtEmail);
        pass = (EditText)findViewById(R.id.edtPassword);
        edad = (EditText)findViewById(R.id.etxtEdad);
        direccion = (EditText)findViewById(R.id.etxtDireccion);
        nombre = (EditText)findViewById(R.id.etxtNombre);
        mRegister = (Button)findViewById(R.id.register);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        new CreateUser().execute();

    }

    class CreateUser extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registrar.this);
            // pDialog.setMessage("Usuario creado...");
            Toast.makeText(Registrar.this,"Usuario creado",
                    Toast.LENGTH_SHORT).show();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            String vNombre = nombre.getText().toString();
            String vDireccion = direccion.getText().toString();
            String vEdad = edad.getText().toString();
            String vEmail = email.getText().toString();
            String vPassword = pass.getText().toString();
            try {
                // Los parametros en comillas debe ser igual a tu BD
                List params = new ArrayList();
                params.add(new BasicNameValuePair("nombre", vNombre));
                params.add(new BasicNameValuePair("direccion", vDireccion));
                params.add(new BasicNameValuePair("edad", vEdad));
                params.add(new BasicNameValuePair("email", vEmail));
                params.add(new BasicNameValuePair("contrasena", vPassword));
                Log.d("Empezando", "Solicitud!");

                //Posting email data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        REGISTER_URL, "POST", params);

                // full json response
                Log.d("Registrando ", json.toString());
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Usuario Creado!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                }else{
                    Log.d("Fallo el registro!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Registrar.this, file_url, Toast.LENGTH_LONG).show();
            }
        }

    }
}

package app.mamac.albadiya;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by T on 07-12-2016.
 */

public class LoginActivity extends Activity {
    TextView ln_btn;
    EditText email,password;
    ImageView back_btn;
   @Override
  public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.login_activity);
       ln_btn = (TextView) findViewById(R.id.ln_btn);
       email   = (EditText) findViewById(R.id.email);
       password = (EditText) findViewById(R.id.password);
       back_btn = (ImageView) findViewById(R.id.back_btn);
       getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       back_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(LoginActivity.this,HomeActivityScreen.class);
               startActivity(intent);
           }
       });

       ln_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email_string = email.getText().toString();
               String password_string = password.getText().toString();
               if (email_string.equals("")){
                   Toast.makeText(LoginActivity.this,"please enter username",Toast.LENGTH_SHORT).show();
                   email.requestFocus();
               }else if (password_string.equals("")){
                   Toast.makeText(LoginActivity.this,"please enter password",Toast.LENGTH_SHORT).show();
                   password.requestFocus();
               }else{
                   final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                   progressDialog.setMessage("please wait..");
                   progressDialog.setCancelable(false);
                   progressDialog.show();
                   Ion.with(LoginActivity.this)
                           .load(Settings.SERVER_URL+"login.php")
                           .setBodyParameter("email",email_string)
                           .setBodyParameter("password",password_string)
                           .asJsonObject()
                           .setCallback(new FutureCallback<JsonObject>() {
                               @Override
                               public void onCompleted(Exception e, JsonObject result) {
                                   try {
                                       if (progressDialog!=null)
                                           progressDialog.dismiss();
                                       if (result.get("status").getAsString().equals("Success")){
                                           Settings.SetUserId(LoginActivity.this,result.get("member_id").getAsString());
                                           //Toast.makeText(LoginActivity.this,result.get("name").getAsString(),Toast.LENGTH_SHORT).show();
                                           Intent intent = new Intent(LoginActivity.this,InstaFragment.class);
                                           startActivity(intent);
                                           finish();
                                       }else {
                                           Toast.makeText(LoginActivity.this,result.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                                       }
                                   }catch (Exception e1){
                                       e1.printStackTrace();
                                   }

                               }
                           });

               }
           }
       });


   }
}

package br.com.caelum.fj59.carangos.tasks;

import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import br.com.caelum.fj59.carangos.app.CarangosApplication;
import br.com.caelum.fj59.carangos.gcm.Constantes;
import br.com.caelum.fj59.carangos.gcm.InformacoesDoUsuario;
import br.com.caelum.fj59.carangos.infra.MyLog;
import br.com.caelum.fj59.carangos.webservice.WebClient;

/**
 * Created by android5628 on 21/01/16.
 */
public class RegistraAparelhoTask extends AsyncTask<Void,Void,String> {
   private CarangosApplication app;

    public RegistraAparelhoTask(CarangosApplication app){
        this.app = app;
    }

    @Override
    protected String doInBackground(Void... params) {
        String registrationId=null;
        try{
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this.app);
            registrationId=gcm.register(Constantes.GCM_SERVER_ID);
            MyLog.i("Aparelho registrado com o ID: "+registrationId);

            String email = InformacoesDoUsuario.getEmail(this.app);

            String url = "device/register/"+email+"/"+registrationId;
            WebClient client = new WebClient(url,app);
            client.post();

        } catch (IOException e) {
            e.printStackTrace();
            MyLog.e("Problema no registro do aparelho no server!"+ e.getMessage());
        }

        return registrationId;
    }

    @Override
    protected void onPostExecute(String result) {
        app.lidaComRespostaDoRegistroNoServidor(result);
    }
}

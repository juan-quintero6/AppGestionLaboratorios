package co.edu.unipiloto.appl;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DelayedMessageService extends IntentService {
    public static final String EXTRA_MESSAGE = "Respuesta";
    public static final int NOTIFICATION_ID = 5453;

    public DelayedMessageService() {
        super("DelayedMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Escribir el codigo para manejar el intent
        synchronized (this){
            try {
                wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String texto = intent.getStringExtra(EXTRA_MESSAGE);
        mostrarTexto(texto);

    }

    private void mostrarTexto(String texto) {
        //Log.v("Delayed Message Service","El mensaje es " +texto);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("12345", "Nombre del Canal", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Descripción del Canal");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"12345")
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setContentTitle(getString(R.string.respuesta))
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[] {0,1000})
                .setAutoCancel(true);
        Intent actionIntent = new Intent(this,MainActivityUsuario.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,
                actionIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(actionPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID,builder.build());
    }

}
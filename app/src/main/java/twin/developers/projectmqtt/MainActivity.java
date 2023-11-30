package twin.developers.projectmqtt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Variables para la base de datos Firebase y los botones de la interfaz
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private Button btnEncender, btnApagar;
    private ImageView imgEncendida, imgApagada;
    private Mqtt mqttManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialización de los botones y el cliente MQTT
        btnEncender = findViewById(R.id.btnEncender);
        btnApagar = findViewById(R.id.btnApagar);
        imgEncendida = findViewById(R.id.AmpolletaEncendida);
        imgApagada = findViewById(R.id.AmpolletaApagada);
        mqttManager = new Mqtt(getApplicationContext());

        // Conecta al cliente MQTT con el broker
        mqttManager.connectToMqttBroker();

        // Establece un listener para el botón de encendido
        btnEncender.setOnClickListener(v -> {
            // Cambia el estado de la luz a "encendido" y publica un mensaje MQTT
            cambiarEstadoLuz("encendido");
            mqttManager.publishMessage("Encender Luz");

            // Muestra la imagen de la luz encendida y oculta la imagen de la luz apagada
            imgEncendida.setVisibility(View.VISIBLE);
            imgApagada.setVisibility(View.GONE);

            // Muestra un mensaje de Toast
            Toast.makeText(getApplicationContext(), "Se encendio la luz de la casa", Toast.LENGTH_SHORT).show();
        });

        // Establece un listener para el botón de apagado
        btnApagar.setOnClickListener(v -> {
            // Cambia el estado de la luz a "apagado" y publica un mensaje MQTT
            cambiarEstadoLuz("apagado");
            mqttManager.publishMessage("Apagar Luz");

            // Muestra la imagen de la luz apagada y oculta la imagen de la luz encendida
            imgEncendida.setVisibility(View.GONE);
            imgApagada.setVisibility(View.VISIBLE);

            // Muestra un mensaje de Toast
            Toast.makeText(getApplicationContext(), "Se apago la luz de la casa", Toast.LENGTH_SHORT).show();
        });
    }

    // Método para cambiar el estado de la luz en la base de datos Firebase
    private void cambiarEstadoLuz(String estado) {
        // Crea un mapa para el nuevo estado de la luz
        Map<String, Object> luz = new HashMap<>();
        luz.put("estado", estado);

        // Obtiene una referencia a la base de datos de Firebase y actualiza el estado de la luz
        DatabaseReference myRef = db.getReference("luz");
        myRef.setValue(luz);
    }
}


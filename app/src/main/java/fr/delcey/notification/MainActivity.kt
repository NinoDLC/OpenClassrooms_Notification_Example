package fr.delcey.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import fr.delcey.notification.databinding.MainActivityBinding
import fr.delcey.notification.utils.viewBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CHANNEL_ID = "MAIN_CHANNEL"
    }

    private val binding by viewBinding { MainActivityBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // see https://developer.android.com/develop/ui/views/notifications/notification-permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }

        // see https://developer.android.com/develop/ui/views/notifications/build-notification#Priority
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel name"
            val descriptionText = "Channel description text"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        binding.fab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_healing_24)
                    .setContentTitle("Je suis un titre de notification")
                    .setContentText("Je suis une description de notification")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                NotificationManagerCompat.from(this).notify(0, builder.build())
            } else {
                Toast.makeText(this, "Y U NO GIVE PERMISSION ?\n(╯°□°)╯︵ ┻━┻", Toast.LENGTH_LONG).show()
            }
        }
    }
}
package com.securevault.app

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAuthenticate = findViewById<Button>(R.id.btnAuthenticate)
        val tvSecretContent = findViewById<TextView>(R.id.tvSecretContent)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(applicationContext, "خطأ في المصادقة: $errString", Toast.LENGTH_SHORT).show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(applicationContext, "تم التحقق بنجاح!", Toast.LENGTH_SHORT).show()
                    // إظهار المحتوى السري وإخفاء زر البصمة
                    tvSecretContent.visibility = View.VISIBLE
                    btnAuthenticate.visibility = View.GONE
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "فشل التعرف على البصمة", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("تسجيل الدخول بالبصمة")
            .setSubtitle("قم بوضع إصبعك على المستشعر لفتح الخزنة")
            .setNegativeButtonText("إلغاء")
            .build()

        btnAuthenticate.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}

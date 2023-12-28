package com.example.jetpackcompose_assignment_one

import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.android.volley.Request.Method
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.jetpackcompose_assignment_one.ui.theme.JetpackComposeAssignmentoneTheme
import org.json.JSONObject
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeAssignmentoneTheme {
                // Pass the function reference without calling it
                LoginScreen(::sendDataToApi,this)
            }
        }
    }


    //lOGIC TO SEND DATA

    private fun sendDataToApi(phoneNumber: String, password: String, context: android.content.Context) {
        // Instantiate the RequestQueue.

        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.29.135/membershipApp/login.php" // Replace with your actual API endpoint URL

        // Create a StringRequest
        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                try {
                    // Convert the response string to a JSON object
                    val jsonResponse = JSONObject(response)

                    // Check the 'status' key in the JSON response
                    val status = jsonResponse.getBoolean("status")

                    if (status) {
                        // Handle the response from the server
                        Toast.makeText(context, "Successfully data sent to server $response", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Response: Data not inserted in the database.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Error parsing JSON response.", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // Handle errors
                Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                // Set the parameters to be sent to the server
                val params: MutableMap<String, String> = HashMap()
                params["mobileNo"] = phoneNumber
                params["password"] = password

                return params
            }
        }

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}




@Composable
fun LoginScreen(sendDataToApi: (String, String, Context) -> Unit,context: Context) {
    var phoneNumber by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Enter Your Mobile no.") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter Your Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                // Call the function reference when the button is clicked
                sendDataToApi(phoneNumber.text.toString().trim(), password.text.toString().trim(), context)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 16.dp),
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Text("Send data to database")
        }
    }
}

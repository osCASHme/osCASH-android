package me.oscash

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create simple text view programmatically
        val textView = TextView(this)
        textView.text = "osCASH.me Android\nVersion 1.0.0-beta\n\nSuccessfully installed!"
        textView.textSize = 20f
        textView.setPadding(50, 100, 50, 100)
        
        setContentView(textView)
    }
}
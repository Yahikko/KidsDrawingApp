package com.example.kidsdrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var brushSizeImgBtn: ImageButton
    private lateinit var brushSizeDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        brushSizeDialog = Dialog(this)
        brushSizeDialog.setContentView(R.layout.dialog_brush_size)
        brushSizeDialog.setTitle("Brush size: ")

        drawingView = findViewById(R.id.drawing_view)
        brushSizeImgBtn = findViewById(R.id.ib_brush)

        brushSizeImgBtn.setOnClickListener { showBrushSizeChooserDialog() }

        drawingView.setBrushSize(5.0f)
    }

    private fun showBrushSizeChooserDialog() {
        val verySmallBtn: ImageButton = brushSizeDialog.findViewById(R.id.ib_very_small_brush)
        brushSizeClickListener(verySmallBtn, 1.0f)

        val smallBtn: ImageButton = brushSizeDialog.findViewById(R.id.ib_small_brush)
        brushSizeClickListener(smallBtn, 5.0f)

        val mediumBtn: ImageButton = brushSizeDialog.findViewById(R.id.ib_medium_brush)
        brushSizeClickListener(mediumBtn, 10.0f)

        val bigBtn: ImageButton = brushSizeDialog.findViewById(R.id.ib_big_brush)
        brushSizeClickListener(bigBtn, 15.0f)

        val veryBigBtn: ImageButton = brushSizeDialog.findViewById(R.id.ib_very_big_brush)
        brushSizeClickListener(veryBigBtn, 20.0f)

        brushSizeDialog.show()
    }

    private fun brushSizeClickListener(imgBtn: ImageButton, brushSize: Float) {
        imgBtn.setOnClickListener {
            drawingView.setBrushSize(brushSize)
            brushSizeDialog.dismiss()
        }
    }
}
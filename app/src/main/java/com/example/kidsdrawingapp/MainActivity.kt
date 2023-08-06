package com.example.kidsdrawingapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var brushSizeImgBtn: ImageButton
    private lateinit var brushSizeDialog: Dialog
    private lateinit var mImageButtonCurrentPaint: ImageButton
    private lateinit var colorsLinearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawing_view)
        brushSizeImgBtn = findViewById(R.id.ib_brush)
        colorsLinearLayout = findViewById(R.id.colors_linear_layout)

        mImageButtonCurrentPaint = colorsLinearLayout[0] as ImageButton
        mImageButtonCurrentPaint.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
        )

        brushSizeImgBtn.setOnClickListener { showBrushSizeChooserDialog() }

        drawingView.setBrushSize(5.0f)
    }

    fun paintClicked(view: View) {
        if (view !== mImageButtonCurrentPaint) {
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView.setColor(colorTag)
            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_pressed)
            )
            mImageButtonCurrentPaint.setImageDrawable(
                ContextCompat.getDrawable(this, R.drawable.pallet_normal)
            )
            mImageButtonCurrentPaint = view
        }
    }

    private fun showBrushSizeChooserDialog() {
        brushSizeDialog = Dialog(this)
        brushSizeDialog.setContentView(R.layout.dialog_brush_size)
        brushSizeDialog.setTitle("Brush size: ")

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
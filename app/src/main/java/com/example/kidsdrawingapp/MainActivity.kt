package com.example.kidsdrawingapp

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var brushSizeImgBtn: ImageButton
    private lateinit var brushSizeDialog: Dialog
    private lateinit var mImageButtonCurrentPaint: ImageButton
    private lateinit var colorsLinearLayout: LinearLayout

    val openGalleryLauncher: ActivityResultLauncher<Intent> = setOpenGalleryLauncher()
    var requestPermission: ActivityResultLauncher<Array<String>> = setRequestPermission()

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

        drawingView.setBrushSize(5.0f)
        brushSizeImgBtn.setOnClickListener { showBrushSizeChooserDialog() }

        val ibGallery: ImageButton = findViewById(R.id.ib_gallery)
        ibGallery.setOnClickListener {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            showRationaleDialog(
                "Kids Drawing App",
                "Kids Drawing App needs to Access Your External Storage"
            )
        } else {
            requestPermission.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                    // TODO: Add writing external storage permission
                )
            )
        }
    }

    private fun setOpenGalleryLauncher(): ActivityResultLauncher<Intent> {
        val openGalleryLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val imageBackground: ImageView = findViewById(R.id.background_image_view)
                    imageBackground.setImageURI(result.data?.data)
                }
            }
        return openGalleryLauncher
    }

    private fun setRequestPermission(): ActivityResultLauncher<Array<String>> {
        val requestPermission: ActivityResultLauncher<Array<String>> =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                permission.entries.forEach {
                    val permissionName = it.key
                    val isGranted = it.value

                    if (isGranted) {
                        val pickIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        openGalleryLauncher.launch(pickIntent)
                    } else {
                        if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            Toast.makeText(
                                this,
                                "You just denied the permission",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        return requestPermission
    }

    private fun showRationaleDialog(
        title: String,
        message: String
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
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
package com.example.chrcam2

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager

import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

//2019-03-25: Kilder:
//https://demonuts.com/pick-image-gallery-camera-android-kotlin/
//https://www.techotopia.com/index.php/Kotlin_-_Video_Recording_and_Image_Capture_on_Android_using_Camera_Intents

import com.example.chrcam2.mediaPack.Picture as PictureImp

class MainActivity : AppCompatActivity() {

    private var BTN: Button? = null
    private var IVW: ImageView? = null
    private var TVW: TextView? = null

    private val GALLERY = 1
    private val CAMERA = 2
    private val VIDEO = 3

    companion object {
        private val IMAGE_DIRECTORY = "/kotlin_hold_B"
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BTN = findViewById<View>(R.id.btn) as Button
        IVW = findViewById<View>(R.id.iv) as ImageView

        TVW = findViewById<View>(R.id.textView2) as TextView

        TVW!!.setOnClickListener{
            //TVW!!.text == "det virker"
            Toast.makeText(this@MainActivity, "det virker!", Toast.LENGTH_SHORT).show()
        }


        BTN!!.setOnClickListener { showPictureDialog() }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera", "Capture video from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
                2 -> filmVideoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun filmVideoFromCamera() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO)
    }


    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    IVW!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        }
        else if (requestCode == CAMERA)
        {
            val thumbnail = data!!.extras!!.get("data") as Bitmap
            IVW!!.setImageBitmap(thumbnail)
            // billede gemmes til: /storage/emulated/0/kotlin_hold_B/
            saveImage(thumbnail)
            Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
        else if (requestCode == VIDEO){

                if (data != null) {
                    val videoUri = data.data
                    if (resultCode == Activity.RESULT_OK) {
                        // Videoen gemmes her: /storage/emulated/0/DCIM/Camera/VID_20190325_175604.mp4
                        Toast.makeText(this, "Video saved to:\n"
                                + videoUri, Toast.LENGTH_LONG).show()

                        val videoView = findViewById<VideoView>(R.id.chrVideo)
                        val mediaController = MediaController(this)
                        mediaController.setAnchorView(videoView)
                        videoView.setMediaController(mediaController)
                        videoView.setVideoURI(videoUri)
                        videoView.requestFocus()
                        videoView.start()

                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Toast.makeText(this, "Video recording cancelled.",
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Failed to record video",
                            Toast.LENGTH_LONG).show()
                    }
                }
                //jeg proever at gemme videoen.



        }
    }

    fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }


}
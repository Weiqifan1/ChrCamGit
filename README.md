# ChrCamGit
nye tests af mulighedderne med camera i android.
(Jeg har forsøgt at fjerne overflødig kode)

Hent billede fra filsystemet og vis det på skærmen
-

```
  //iv står for ImageView og er viewets id
  IVW = findViewById<View>(R.id.iv) as ImageView
  val GALLERY = 1
  val Intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
  startActivityForResult(Intent, GALLERY)
  
  public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val contentURI = data!!.data
    if (requestCode == GALLERY){
       val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
       IVW!!.setImageBitmap(bitmap)
    }
  }
  
```

Tag et billede, gem det og vis det på skærmen
-

```
  //iv står for ImageView og er viewets id
  IVW = findViewById<View>(R.id.iv) as ImageView
  val CAMERA = 2
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  startActivityForResult(intent, CAMERA)

  public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val contentURI = data!!.data
    if (requestCode == CAMERA){
      val bitmap = data!!.extras!!.get("data") as Bitmap
      IVW!!.setImageBitmap(bitmap)
    }
  }
```

Film en video og vis den på skærmen (gemmes automatisk)
-

```
  val videoView = findViewById<VideoView>(R.id.chrVideo)
  private val VIDEO = 3
  val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
  startActivityForResult(intent, VIDEO)
    
  public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    val videoUri = data.data
    if (requestCode == VIDEO){
      val mediaController = MediaController(this)
      mediaController.setAnchorView(videoView)
      videoView.setMediaController(mediaController)
      videoView.setVideoURI(videoUri)
      videoView.requestFocus()
      videoView.start()
    }
  }
```

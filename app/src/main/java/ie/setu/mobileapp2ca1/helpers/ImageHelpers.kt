package ie.setu.mobileapp2ca1.helpers

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import ie.setu.mobileapp2ca1.R
import timber.log.Timber.i

fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>, context: Context) {
    var imagePickerTargetIntent = Intent(Intent.ACTION_PICK)

    imagePickerTargetIntent.action = Intent.ACTION_OPEN_DOCUMENT
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    imagePickerTargetIntent.type = "image/*"
    imagePickerTargetIntent = Intent.createChooser(imagePickerTargetIntent,
        context.getString(R.string.select_trackImage))
    intentLauncher.launch(imagePickerTargetIntent)
}

fun showCamera(intentLauncher: ActivityResultLauncher<Intent>,context: Context) {
    var takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    try {
        takePictureIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        takePictureIntent = Intent.createChooser(takePictureIntent,
            context.getString(R.string.select_trackImage))
        intentLauncher.launch(takePictureIntent)
    } catch (e: ActivityNotFoundException) {
        i("Can't open camera")
    }
}

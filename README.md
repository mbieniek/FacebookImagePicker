# FacebookImagePicker

An Android Facebook image picker built using Kotlin, RxJava2, and Facebook's Graph API. The library allows the user to browse through their Facebook albums and images. This will require the Facebook app being used to have the "user_photos" permission. The library allows the user to select a number of photos and will return the associated preview and source URLs for the photo(s) selected. The sample app shows an example of how to use the selected Facebook image data to download and use the image(s) within an ImageView.

# Demo
Selecting Facebook images once authenticated by Facebook: </br>
![](https://i.imgur.com/OAwDm00.gif) </br>

Allowing the library to handle authentication if the user hasn't given your app Facebook permission to access photos. Note that the app name/title that the library displays will be based on your facebook_app_id. </br>
![](https://i.imgur.com/WBaV0Ut.gif) </br>

# Usage

## Setup
Your app will need to be configured and registered with Facebook. Click [here](https://developers.facebook.com/docs/android/) to learn more if you haven't done this yet. See the sample app for a practical example of how to use this.

## Starting and Getting Results

To start the activity:
```kotlin
val intent = Intent(this, FacebookAlbumPickerActivity::class.java)
startActivityForResult(intent, FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE)
```

Handling results:
```kotlin
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == FACEBOOK_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
        val rawImages = data.getParcelableArrayExtra(FACEBOOK_IMAGE_RESULT_KEY)
        if (rawImages != null && rawImages.size > 0) {
            for (i in rawImages.indices) {
                //Here you have access to the picture url to do as you please (either download the picture to device
                //or pass the url onto another app/library. For an example, we'll download and display the images
                //in a carousel view.
                val picture = rawImages[i] as FacebookPicture

                dataManager.downloadFileByUrlToFileDir(picture.sourceUrl)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it != null) {
                                downloadedImageList.add(it)
                                carouselView.pageCount = downloadedImageList.size
                                Toast.makeText(this, "Image #" + i + " downloaded successfully!", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this, "Image #" + i + " failed to download!", Toast.LENGTH_LONG).show()
                            }
                        })
            }
        }
    }
}
```
    
## Customization
The level of customization is pretty limited right now. There is a singleton in the library called FacebookImagePickerSettings which contains a bunch of tweakable variables.

```kotlin
object FacebookImagePickerSettings {
    var maximumSelectableImages :Int = 6
    var imageGridSpanCount :Int = 3
    var albumActivityTitle : String = "Album(s)"
    var imagesSelectedText : String = "Select (%d)"
    var maximumImagesSelectedText : String = "You\'ve reached the maximum limit of photos that can be selected!"
    var placeholderDrawableColor : String = "#000000"
}
```

To edit these in your app, you should be able to just change the variable like so:

```kotlin
FacebookImagePickerSettings.placeholderDrawableColor = "#3F51B5"
FacebookImagePickerSettings.imageGridSpanCount = 2
```

package com.example.puranabi

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.util.Log
import android.widget.ImageView
import org.opencv.android.OpenCVLoader //OpenCVLoaderのインポート
import org.opencv.core.Mat
import org.opencv.core.CvType
import org.opencv.imgproc.Imgproc
import org.opencv.android.Utils
import org.opencv.imgcodecs.Imgcodecs
import java.io.InputStream
import android.graphics.Bitmap

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "OpenCV initialization succeeded")
        } else {
            Log.d("OpenCV", "OpenCV initialization failed")
        }

        // 画像を表示するImageViewを取得
        val imageView: ImageView = findViewById(R.id.imageView)

        // リソースIDを使って画像を読み込む
        val originalImage = loadImageFromResources(R.drawable.regiscan)

        // グレースケール変換
        val grayscaleImage = Mat()
        Imgproc.cvtColor(originalImage, grayscaleImage, Imgproc.COLOR_BGR2GRAY)

        // 二値化処理
        val binaryImage = Mat()
        Imgproc.threshold(grayscaleImage, binaryImage, 127.0, 255.0, Imgproc.THRESH_BINARY)

        // 結果をBitmapに変換
        val binaryBitmap = matToBitmap(binaryImage)

        // ImageViewに画像をセット
        imageView.setImageBitmap(binaryBitmap)
    }

    // 画像をリソースから読み込むメソッド
    private fun loadImageFromResources(imageResId: Int): Mat {
        // リソースから画像を開く
        val inputStream: InputStream = resources.openRawResource(imageResId)
        val byteArray = inputStream.readBytes()

        // バイト配列をMatに変換して画像をデコード
        return Imgcodecs.imdecode(Mat(1, byteArray.size, CvType.CV_8U).apply {
            put(0, 0, byteArray) // Matにバイトデータを設定
        }, Imgcodecs.IMREAD_COLOR)
    }

    // MatからBitmapに変換するメソッド
    private fun matToBitmap(mat: Mat): Bitmap {
        val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(mat, bitmap)
        return bitmap
    }
}

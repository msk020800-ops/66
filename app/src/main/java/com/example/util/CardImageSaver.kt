package com.example.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.FileProvider
import com.example.model.ChuseokTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**
 * 4:5 비율(1080x1350)의 고화질 모바일 추석 인사카드를 비트맵으로 렌더링하고
 * 스마트폰 갤러리(MediaStore)에 저장 및 카카오톡/메시지 공유를 지원하는 유틸리티
 */
object CardImageSaver {

    const val CARD_WIDTH = 1080
    const val CARD_HEIGHT = 1350

    suspend fun renderCardBitmap(
        context: Context,
        theme: ChuseokTheme,
        recipientText: String,
        greetingText: String,
        bottomBlessing: String = "즐겁고 행복한 한가위 보내세요"
    ): Bitmap = withContext(Dispatchers.Default) {
        val bitmap = Bitmap.createBitmap(CARD_WIDTH, CARD_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 1. 배경 렌더링
        val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val bgStart = theme.bgStartColor.let {
            AndroidColor.argb((it.alpha * 255).toInt(), (it.red * 255).toInt(), (it.green * 255).toInt(), (it.blue * 255).toInt())
        }
        val bgEnd = theme.bgEndColor.let {
            AndroidColor.argb((it.alpha * 255).toInt(), (it.red * 255).toInt(), (it.green * 255).toInt(), (it.blue * 255).toInt())
        }
        bgPaint.shader = LinearGradient(0f, 0f, 0f, CARD_HEIGHT.toFloat(), bgStart, bgEnd, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, CARD_WIDTH.toFloat(), CARD_HEIGHT.toFloat(), bgPaint)

        // 테마 이미지 배경이 있는 경우 부드럽게 합성
        if (theme.drawableResId != null) {
            try {
                val rawImage = BitmapFactory.decodeResource(context.resources, theme.drawableResId)
                if (rawImage != null) {
                    val srcRect = Rect(0, 0, rawImage.width, rawImage.height)
                    val dstRect = Rect(0, 0, CARD_WIDTH, (CARD_HEIGHT * 0.58f).toInt())
                    val imagePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
                        alpha = 190
                    }
                    canvas.drawBitmap(rawImage, srcRect, dstRect, imagePaint)

                    // 이미지 아래쪽으로 자연스럽게 어두워지는 그라디언트 오버레이
                    val overlayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                    overlayPaint.shader = LinearGradient(
                        0f, (CARD_HEIGHT * 0.35f),
                        0f, CARD_HEIGHT.toFloat(),
                        AndroidColor.TRANSPARENT,
                        bgStart,
                        Shader.TileMode.CLAMP
                    )
                    canvas.drawRect(0f, 0f, CARD_WIDTH.toFloat(), CARD_HEIGHT.toFloat(), overlayPaint)
                }
            } catch (e: Exception) {
                // Ignore and proceed with vector elements
            }
        } else {
            // 이미지가 없는 테마(한지, 가족, 토끼 등)는 은은한 보름달과 전통 문양 렌더링
            val moonCenterY = CARD_HEIGHT * 0.28f
            val moonCenterX = CARD_WIDTH * 0.5f
            val moonRadius = CARD_WIDTH * 0.22f

            // 달무리 (Glow)
            val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = RadialGradient(
                    moonCenterX, moonCenterY, moonRadius * 1.8f,
                    AndroidColor.argb(100, 255, 220, 110),
                    AndroidColor.TRANSPARENT,
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawCircle(moonCenterX, moonCenterY, moonRadius * 1.8f, glowPaint)

            // 둥근 황금 보름달
            val moonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = RadialGradient(
                    moonCenterX - moonRadius * 0.3f, moonCenterY - moonRadius * 0.3f, moonRadius,
                    AndroidColor.rgb(255, 245, 180),
                    AndroidColor.rgb(240, 185, 55),
                    Shader.TileMode.CLAMP
                )
            }
            canvas.drawCircle(moonCenterX, moonCenterY, moonRadius, moonPaint)
        }

        // 2. 전통 외곽 테두리 (Traditional Hanji Gold Frame)
        val frameMargin = 40f
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.argb(160, 225, 185, 80)
            style = Paint.Style.STROKE
            strokeWidth = 4f
        }
        val innerMargin = 52f
        val innerBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.argb(100, 245, 215, 120)
            style = Paint.Style.STROKE
            strokeWidth = 2f
        }
        canvas.drawRoundRect(RectF(frameMargin, frameMargin, CARD_WIDTH - frameMargin, CARD_HEIGHT - frameMargin), 24f, 24f, borderPaint)
        canvas.drawRoundRect(RectF(innerMargin, innerMargin, CARD_WIDTH - innerMargin, CARD_HEIGHT - innerMargin), 16f, 16f, innerBorderPaint)

        // 네 모서리 전통 장식 십자/문양
        val cornerSize = 28f
        val cornerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(240, 195, 75)
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }
        fun drawCorner(cx: Float, cy: Float, dx: Float, dy: Float) {
            canvas.drawLine(cx, cy, cx + dx * cornerSize, cy, cornerPaint)
            canvas.drawLine(cx, cy, cx, cy + dy * cornerSize, cornerPaint)
        }
        drawCorner(innerMargin + 8f, innerMargin + 8f, 1f, 1f)
        drawCorner(CARD_WIDTH - innerMargin - 8f, innerMargin + 8f, -1f, 1f)
        drawCorner(innerMargin + 8f, CARD_HEIGHT - innerMargin - 8f, 1f, -1f)
        drawCorner(CARD_WIDTH - innerMargin - 8f, CARD_HEIGHT - innerMargin - 8f, -1f, -1f)

        // 3. 상단 테마 뱃지
        val badgeText = "${theme.icon} ${theme.title}"
        val badgePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(255, 235, 170)
            textSize = 34f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(badgeText, CARD_WIDTH / 2f, 110f, badgePaint)

        // 4. 받는 사람 (Recipient)
        val recipientPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(255, 225, 90)
            textSize = 50f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        val recipientY = CARD_HEIGHT * 0.44f
        canvas.drawText(recipientText, CARD_WIDTH / 2f, recipientY, recipientPaint)

        // 받는 분 밑줄 장식
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.argb(120, 245, 200, 80)
            strokeWidth = 3f
        }
        val lineWidth = 240f
        canvas.drawLine(
            (CARD_WIDTH - lineWidth) / 2f,
            recipientY + 24f,
            (CARD_WIDTH + lineWidth) / 2f,
            recipientY + 24f,
            linePaint
        )

        // 5. 인사말 본문 (Greeting Message Body)
        // 50~70대 어르신들을 위한 큼직하고 여유로운 한글 타이포그래피
        val bodyTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(255, 252, 242)
            textSize = 42f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val textWidth = (CARD_WIDTH - 220)
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder.obtain(greetingText, 0, greetingText.length, bodyTextPaint, textWidth)
                .setAlignment(Layout.Alignment.ALIGN_CENTER)
                .setLineSpacing(22f, 1.25f)
                .build()
        } else {
            @Suppress("DEPRECATION")
            StaticLayout(
                greetingText,
                bodyTextPaint,
                textWidth,
                Layout.Alignment.ALIGN_CENTER,
                1.25f,
                22f,
                false
            )
        }

        canvas.save()
        val textStartY = recipientY + 80f
        canvas.translate((CARD_WIDTH - textWidth) / 2f, textStartY)
        staticLayout.draw(canvas)
        canvas.restore()

        // 6. 하단 덕담 리본 & 전통 낙관
        val bottomY = CARD_HEIGHT - 170f

        // 하단 대표 문구 ("즐겁고 행복한 한가위 보내세요")
        val bottomBlessingPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(255, 215, 80)
            textSize = 38f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(bottomBlessing, CARD_WIDTH / 2f, bottomY, bottomBlessingPaint)

        // 전통 붉은 낙관 (한가위)
        val sealSize = 56f
        val sealRight = CARD_WIDTH - 90f
        val sealBottom = CARD_HEIGHT - 90f
        val sealRect = RectF(sealRight - sealSize, sealBottom - sealSize, sealRight, sealBottom)

        val sealPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(180, 40, 40)
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(sealRect, 10f, 10f, sealPaint)

        val sealBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.rgb(235, 170, 70)
            style = Paint.Style.STROKE
            strokeWidth = 2.5f
        }
        canvas.drawRoundRect(sealRect, 10f, 10f, sealBorder)

        val sealTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.WHITE
            textSize = 24f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("한가위", sealRect.centerX(), sealRect.centerY() + 8f, sealTextPaint)

        bitmap
    }

    /**
     * 렌더링된 비트맵을 기기의 갤러리(MediaStore / Pictures / ChuseokCards)에 저장
     */
    suspend fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        title: String = "추석_인사카드_${System.currentTimeMillis()}"
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val resolver = context.contentResolver
            val imageUri: Uri?
            val outputStream: OutputStream?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/ChuseokCards")
                    put(MediaStore.MediaColumns.IS_PENDING, 1)
                }

                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                if (imageUri == null) {
                    return@withContext Result.failure(Exception("갤러리 저장 경로를 생성할 수 없습니다."))
                }

                outputStream = resolver.openOutputStream(imageUri)
                if (outputStream == null) {
                    return@withContext Result.failure(Exception("출력 스트림을 열 수 없습니다."))
                }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                outputStream.flush()
                outputStream.close()

                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(imageUri, contentValues, null, null)
            } else {
                val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val appDir = File(imagesDir, "ChuseokCards")
                if (!appDir.exists()) appDir.mkdirs()

                val imageFile = File(appDir, "$title.jpg")
                outputStream = FileOutputStream(imageFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                outputStream.flush()
                outputStream.close()

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.DISPLAY_NAME, "$title.jpg")
                }
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            }

            if (imageUri != null) {
                Result.success(imageUri)
            } else {
                Result.failure(Exception("저장된 이미지 주소를 가져오지 못했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 카카오톡, 문자메시지 등으로 즉시 공유할 수 있는 Intent 생성
     */
    fun shareCard(context: Context, imageUri: Uri, message: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, imageUri)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "추석 인사카드 보내기"))
    }
}

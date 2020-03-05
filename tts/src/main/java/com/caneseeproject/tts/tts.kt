package com.caneseeproject.tts

import android.speech.tts.TextToSpeech
import com.caneseeaproject.computervision.*
import com.caneseeproject.obstacledetection.*
import com.caneseeproject.sensorPortals.*

interface TextToSpeechInterface {
    fun notify(recent: NotificationType)
}

class TextToSpeachClass : TextToSpeechInterface {
    var tts: TextToSpeech? = null
    //    tts fun
    private fun speakOut(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_ADD, null)
    }

    override fun notify ( recent : NotificationType ) {
        when (recent) {
            is NotificationType.SensorNotification -> when (recent.state) {
                is CVInput.ModeChange -> when (recent.state.mode){
                    1 -> speakOut("OCR is Activated now")
                    2 -> speakOut("scene Description")
                    3 -> speakOut("face recognition is turned on")
                    4 -> speakOut("emotion recognition is turned on")
                    5 -> speakOut("object detection is turned on")
                    else -> speakOut("Please, Enter a number from 1 to 5")
                }
                is ODInput.RangeControl -> speakOut("Distance Detection is turned on")
            }

            is NotificationType.SensorContent -> when (recent.context) {
                is Vision.OCR -> speakOut(recent.context.transcript)
                is Vision.Facial -> speakOut(recent.context.prettyFace)
                is Vision.Emotion -> speakOut(recent.context.emotion)
                is Vision.ObjectDetection -> speakOut(recent.context.objects.toString())
                is Vision.Scenery -> speakOut(recent.context.scene)
                is ODReading.ObstacleDistance -> speakOut(recent.context.distance.toString())
            }

            is NotificationType.AppMessage -> speakOut(recent.appTutorial)
        }
    }
}
sealed class NotificationType{
    class AppMessage ( val appTutorial:String ) : NotificationType()
    class SensorNotification ( val state :SensorInput ) : NotificationType()
    class SensorContent ( val context :SensorReading ) : NotificationType()
}

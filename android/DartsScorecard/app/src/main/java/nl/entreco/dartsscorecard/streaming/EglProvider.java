package nl.entreco.dartsscorecard.streaming;

import org.webrtc.EglBase;

class EglProvider {

    static EglBase get(){
        return EglBase.create();
    }
}

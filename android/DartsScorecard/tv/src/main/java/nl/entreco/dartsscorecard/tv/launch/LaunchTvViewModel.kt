package nl.entreco.dartsscorecard.tv.launch

import nl.entreco.dartsscorecard.tv.base.TvViewModel
import nl.entreco.domain.stream.RegisterStreamReceiverUsecase
import nl.entreco.shared.log.Logger
import javax.inject.Inject

class LaunchTvViewModel @Inject constructor(
        private val logger: Logger,
        registerStreamReceiverUsecase: RegisterStreamReceiverUsecase) : TvViewModel() {

    init {
        registerStreamReceiverUsecase.exec(
                {
                    logger.d("BINGO ON TV $it")
                },
                {
                    logger.e("BINGO -> OOPS, ERR -> ON TV $it")
                })
    }
}
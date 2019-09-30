package nl.entreco.data.sound

import nl.entreco.data.R
import nl.entreco.domain.play.mastercaller.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by entreco on 14/03/2018.
 */
class SoundMapperTest {

    private val subject = SoundMapper()

    @Test
    fun `it should map None`() {
        assertEquals(0, subject.toRaw(None))
    }

    @Test
    fun `it should map Fx00`() {
        assertEquals(R.raw.dsc_pro0, subject.toRaw(Fx00))
    }

    @Test
    fun `it should map Fx01`() {
        assertEquals(R.raw.dsc_pro1, subject.toRaw(Fx01))
    }

    @Test
    fun `it should map Fx180`() {
        assertEquals(R.raw.dsc_pro180, subject.toRaw(Fx180))
    }

    @Test
    fun `it should map FxStart`() {
        assertEquals(R.raw.dsc_proletsplaydarts, subject.toRaw(FxStart))
    }

    @Test
    fun `it should map FxLeg`() {
        assertEquals(R.raw.dsc_proleg, subject.toRaw(FxLeg))
    }

    @Test
    fun `it should map FxSet`() {
        assertEquals(R.raw.dsc_proset, subject.toRaw(FxSet))
    }

    @Test
    fun `it should map FxMatch`() {
        assertEquals(R.raw.dsc_progameshot, subject.toRaw(FxMatch))
    }

    @Test
    fun `it should map others`() {
        assertEquals(R.raw.dsc_pro2, subject.toRaw(Fx02))
        assertEquals(R.raw.dsc_pro3, subject.toRaw(Fx03))
        assertEquals(R.raw.dsc_pro4, subject.toRaw(Fx04))
        assertEquals(R.raw.dsc_pro5, subject.toRaw(Fx05))
        assertEquals(R.raw.dsc_pro6, subject.toRaw(Fx06))
        assertEquals(R.raw.dsc_pro7, subject.toRaw(Fx07))
        assertEquals(R.raw.dsc_pro8, subject.toRaw(Fx08))
        assertEquals(R.raw.dsc_pro9, subject.toRaw(Fx09))
        assertEquals(R.raw.dsc_pro10, subject.toRaw(Fx10))
        assertEquals(R.raw.dsc_pro11, subject.toRaw(Fx11))
        assertEquals(R.raw.dsc_pro12, subject.toRaw(Fx12))
        assertEquals(R.raw.dsc_pro13, subject.toRaw(Fx13))
        assertEquals(R.raw.dsc_pro14, subject.toRaw(Fx14))
        assertEquals(R.raw.dsc_pro15, subject.toRaw(Fx15))
        assertEquals(R.raw.dsc_pro16, subject.toRaw(Fx16))
        assertEquals(R.raw.dsc_pro17, subject.toRaw(Fx17))
        assertEquals(R.raw.dsc_pro18, subject.toRaw(Fx18))
        assertEquals(R.raw.dsc_pro19, subject.toRaw(Fx19))
        assertEquals(R.raw.dsc_pro20, subject.toRaw(Fx20))
        assertEquals(R.raw.dsc_pro21, subject.toRaw(Fx21))
        assertEquals(R.raw.dsc_pro22, subject.toRaw(Fx22))
        assertEquals(R.raw.dsc_pro23, subject.toRaw(Fx23))
        assertEquals(R.raw.dsc_pro24, subject.toRaw(Fx24))
        assertEquals(R.raw.dsc_pro25, subject.toRaw(Fx25))
        assertEquals(R.raw.dsc_pro26, subject.toRaw(Fx26))
        assertEquals(R.raw.dsc_pro27, subject.toRaw(Fx27))
        assertEquals(R.raw.dsc_pro28, subject.toRaw(Fx28))
        assertEquals(R.raw.dsc_pro29, subject.toRaw(Fx29))
        assertEquals(R.raw.dsc_pro30, subject.toRaw(Fx30))
        assertEquals(R.raw.dsc_pro31, subject.toRaw(Fx31))
        assertEquals(R.raw.dsc_pro32, subject.toRaw(Fx32))
        assertEquals(R.raw.dsc_pro33, subject.toRaw(Fx33))
        assertEquals(R.raw.dsc_pro34, subject.toRaw(Fx34))
        assertEquals(R.raw.dsc_pro35, subject.toRaw(Fx35))
        assertEquals(R.raw.dsc_pro36, subject.toRaw(Fx36))
        assertEquals(R.raw.dsc_pro37, subject.toRaw(Fx37))
        assertEquals(R.raw.dsc_pro38, subject.toRaw(Fx38))
        assertEquals(R.raw.dsc_pro39, subject.toRaw(Fx39))
        assertEquals(R.raw.dsc_pro40, subject.toRaw(Fx40))
        assertEquals(R.raw.dsc_pro41, subject.toRaw(Fx41))
        assertEquals(R.raw.dsc_pro42, subject.toRaw(Fx42))
        assertEquals(R.raw.dsc_pro43, subject.toRaw(Fx43))
        assertEquals(R.raw.dsc_pro44, subject.toRaw(Fx44))
        assertEquals(R.raw.dsc_pro45, subject.toRaw(Fx45))
        assertEquals(R.raw.dsc_pro46, subject.toRaw(Fx46))
        assertEquals(R.raw.dsc_pro47, subject.toRaw(Fx47))
        assertEquals(R.raw.dsc_pro48, subject.toRaw(Fx48))
        assertEquals(R.raw.dsc_pro49, subject.toRaw(Fx49))
        assertEquals(R.raw.dsc_pro50, subject.toRaw(Fx50))
        assertEquals(R.raw.dsc_pro51, subject.toRaw(Fx51))
        assertEquals(R.raw.dsc_pro52, subject.toRaw(Fx52))
        assertEquals(R.raw.dsc_pro53, subject.toRaw(Fx53))
        assertEquals(R.raw.dsc_pro54, subject.toRaw(Fx54))
        assertEquals(R.raw.dsc_pro55, subject.toRaw(Fx55))
        assertEquals(R.raw.dsc_pro56, subject.toRaw(Fx56))
        assertEquals(R.raw.dsc_pro57, subject.toRaw(Fx57))
        assertEquals(R.raw.dsc_pro58, subject.toRaw(Fx58))
        assertEquals(R.raw.dsc_pro59, subject.toRaw(Fx59))
        assertEquals(R.raw.dsc_pro60, subject.toRaw(Fx60))
        assertEquals(R.raw.dsc_pro61, subject.toRaw(Fx61))
        assertEquals(R.raw.dsc_pro62, subject.toRaw(Fx62))
        assertEquals(R.raw.dsc_pro63, subject.toRaw(Fx63))
        assertEquals(R.raw.dsc_pro64, subject.toRaw(Fx64))
        assertEquals(R.raw.dsc_pro65, subject.toRaw(Fx65))
        assertEquals(R.raw.dsc_pro66, subject.toRaw(Fx66))
        assertEquals(R.raw.dsc_pro67, subject.toRaw(Fx67))
        assertEquals(R.raw.dsc_pro68, subject.toRaw(Fx68))
        assertEquals(R.raw.dsc_pro69, subject.toRaw(Fx69))
        assertEquals(R.raw.dsc_pro70, subject.toRaw(Fx70))
        assertEquals(R.raw.dsc_pro71, subject.toRaw(Fx71))
        assertEquals(R.raw.dsc_pro72, subject.toRaw(Fx72))
        assertEquals(R.raw.dsc_pro73, subject.toRaw(Fx73))
        assertEquals(R.raw.dsc_pro74, subject.toRaw(Fx74))
        assertEquals(R.raw.dsc_pro75, subject.toRaw(Fx75))
        assertEquals(R.raw.dsc_pro76, subject.toRaw(Fx76))
        assertEquals(R.raw.dsc_pro77, subject.toRaw(Fx77))
        assertEquals(R.raw.dsc_pro78, subject.toRaw(Fx78))
        assertEquals(R.raw.dsc_pro79, subject.toRaw(Fx79))
        assertEquals(R.raw.dsc_pro80, subject.toRaw(Fx80))
        assertEquals(R.raw.dsc_pro81, subject.toRaw(Fx81))
        assertEquals(R.raw.dsc_pro82, subject.toRaw(Fx82))
        assertEquals(R.raw.dsc_pro83, subject.toRaw(Fx83))
        assertEquals(R.raw.dsc_pro84, subject.toRaw(Fx84))
        assertEquals(R.raw.dsc_pro85, subject.toRaw(Fx85))
        assertEquals(R.raw.dsc_pro86, subject.toRaw(Fx86))
        assertEquals(R.raw.dsc_pro87, subject.toRaw(Fx87))
        assertEquals(R.raw.dsc_pro88, subject.toRaw(Fx88))
        assertEquals(R.raw.dsc_pro89, subject.toRaw(Fx89))
        assertEquals(R.raw.dsc_pro90, subject.toRaw(Fx90))
        assertEquals(R.raw.dsc_pro91, subject.toRaw(Fx91))
        assertEquals(R.raw.dsc_pro92, subject.toRaw(Fx92))
        assertEquals(R.raw.dsc_pro93, subject.toRaw(Fx93))
        assertEquals(R.raw.dsc_pro94, subject.toRaw(Fx94))
        assertEquals(R.raw.dsc_pro95, subject.toRaw(Fx95))
        assertEquals(R.raw.dsc_pro96, subject.toRaw(Fx96))
        assertEquals(R.raw.dsc_pro97, subject.toRaw(Fx97))
        assertEquals(R.raw.dsc_pro98, subject.toRaw(Fx98))
        assertEquals(R.raw.dsc_pro99, subject.toRaw(Fx99))
        assertEquals(R.raw.dsc_pro100, subject.toRaw(Fx100))
        assertEquals(R.raw.dsc_pro101, subject.toRaw(Fx101))
        assertEquals(R.raw.dsc_pro102, subject.toRaw(Fx102))
        assertEquals(R.raw.dsc_pro103, subject.toRaw(Fx103))
        assertEquals(R.raw.dsc_pro104, subject.toRaw(Fx104))
        assertEquals(R.raw.dsc_pro105, subject.toRaw(Fx105))
        assertEquals(R.raw.dsc_pro106, subject.toRaw(Fx106))
        assertEquals(R.raw.dsc_pro107, subject.toRaw(Fx107))
        assertEquals(R.raw.dsc_pro108, subject.toRaw(Fx108))
        assertEquals(R.raw.dsc_pro109, subject.toRaw(Fx109))
        assertEquals(R.raw.dsc_pro110, subject.toRaw(Fx110))
        assertEquals(R.raw.dsc_pro111, subject.toRaw(Fx111))
        assertEquals(R.raw.dsc_pro112, subject.toRaw(Fx112))
        assertEquals(R.raw.dsc_pro113, subject.toRaw(Fx113))
        assertEquals(R.raw.dsc_pro114, subject.toRaw(Fx114))
        assertEquals(R.raw.dsc_pro115, subject.toRaw(Fx115))
        assertEquals(R.raw.dsc_pro116, subject.toRaw(Fx116))
        assertEquals(R.raw.dsc_pro117, subject.toRaw(Fx117))
        assertEquals(R.raw.dsc_pro118, subject.toRaw(Fx118))
        assertEquals(R.raw.dsc_pro119, subject.toRaw(Fx119))
        assertEquals(R.raw.dsc_pro120, subject.toRaw(Fx120))
        assertEquals(R.raw.dsc_pro121, subject.toRaw(Fx121))
        assertEquals(R.raw.dsc_pro122, subject.toRaw(Fx122))
        assertEquals(R.raw.dsc_pro123, subject.toRaw(Fx123))
        assertEquals(R.raw.dsc_pro124, subject.toRaw(Fx124))
        assertEquals(R.raw.dsc_pro125, subject.toRaw(Fx125))
        assertEquals(R.raw.dsc_pro126, subject.toRaw(Fx126))
        assertEquals(R.raw.dsc_pro127, subject.toRaw(Fx127))
        assertEquals(R.raw.dsc_pro128, subject.toRaw(Fx128))
        assertEquals(R.raw.dsc_pro129, subject.toRaw(Fx129))
        assertEquals(R.raw.dsc_pro130, subject.toRaw(Fx130))
        assertEquals(R.raw.dsc_pro131, subject.toRaw(Fx131))
        assertEquals(R.raw.dsc_pro132, subject.toRaw(Fx132))
        assertEquals(R.raw.dsc_pro133, subject.toRaw(Fx133))
        assertEquals(R.raw.dsc_pro134, subject.toRaw(Fx134))
        assertEquals(R.raw.dsc_pro135, subject.toRaw(Fx135))
        assertEquals(R.raw.dsc_pro136, subject.toRaw(Fx136))
        assertEquals(R.raw.dsc_pro137, subject.toRaw(Fx137))
        assertEquals(R.raw.dsc_pro138, subject.toRaw(Fx138))
        assertEquals(R.raw.dsc_pro139, subject.toRaw(Fx139))
        assertEquals(R.raw.dsc_pro140, subject.toRaw(Fx140))
        assertEquals(R.raw.dsc_pro141, subject.toRaw(Fx141))
        assertEquals(R.raw.dsc_pro142, subject.toRaw(Fx142))
        assertEquals(R.raw.dsc_pro143, subject.toRaw(Fx143))
        assertEquals(R.raw.dsc_pro144, subject.toRaw(Fx144))
        assertEquals(R.raw.dsc_pro145, subject.toRaw(Fx145))
        assertEquals(R.raw.dsc_pro146, subject.toRaw(Fx146))
        assertEquals(R.raw.dsc_pro147, subject.toRaw(Fx147))
        assertEquals(R.raw.dsc_pro148, subject.toRaw(Fx148))
        assertEquals(R.raw.dsc_pro149, subject.toRaw(Fx149))
        assertEquals(R.raw.dsc_pro150, subject.toRaw(Fx150))
        assertEquals(R.raw.dsc_pro151, subject.toRaw(Fx151))
        assertEquals(R.raw.dsc_pro152, subject.toRaw(Fx152))
        assertEquals(R.raw.dsc_pro153, subject.toRaw(Fx153))
        assertEquals(R.raw.dsc_pro154, subject.toRaw(Fx154))
        assertEquals(R.raw.dsc_pro155, subject.toRaw(Fx155))
        assertEquals(R.raw.dsc_pro156, subject.toRaw(Fx156))
        assertEquals(R.raw.dsc_pro157, subject.toRaw(Fx157))
        assertEquals(R.raw.dsc_pro158, subject.toRaw(Fx158))
        assertEquals(R.raw.dsc_pro159, subject.toRaw(Fx159))
        assertEquals(R.raw.dsc_pro160, subject.toRaw(Fx160))
        assertEquals(R.raw.dsc_pro161, subject.toRaw(Fx161))
        assertEquals(R.raw.dsc_pro162, subject.toRaw(Fx162))
        assertEquals(R.raw.dsc_pro163, subject.toRaw(Fx163))
        assertEquals(R.raw.dsc_pro164, subject.toRaw(Fx164))
        assertEquals(R.raw.dsc_pro165, subject.toRaw(Fx165))
        assertEquals(R.raw.dsc_pro166, subject.toRaw(Fx166))
        assertEquals(R.raw.dsc_pro167, subject.toRaw(Fx167))
        assertEquals(R.raw.dsc_pro168, subject.toRaw(Fx168))
        assertEquals(R.raw.dsc_pro169, subject.toRaw(Fx169))
        assertEquals(R.raw.dsc_pro170, subject.toRaw(Fx170))
        assertEquals(R.raw.dsc_pro171, subject.toRaw(Fx171))
        assertEquals(R.raw.dsc_pro174, subject.toRaw(Fx174))
        assertEquals(R.raw.dsc_pro177, subject.toRaw(Fx177))
        assertEquals(R.raw.dsc_pro180, subject.toRaw(Fx180))
    }

    @Test(expected = IllegalStateException::class)
    fun `it should throw when mapping unknown sounds`() {
        assertEquals(R.raw.dsc_pro0, subject.toRaw(FxTest))
    }

}
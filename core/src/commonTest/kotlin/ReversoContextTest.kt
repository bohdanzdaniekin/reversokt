package io.github.mrnemo.reversokt

import io.github.mrnemo.reversokt.entity.ReversoError
import io.github.mrnemo.reversokt.entity.Service
import io.github.mrnemo.reversokt.entity.language.Language
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ReversoContextTest {

    @Test
    fun `test contextOf with valid response`(): Unit = runBlocking {
        val result = reverso.contextOf(text = "example", source = Language.ENGLISH, target = Language.FRENCH)

        assertTrue(result.isRight())
        result.map { context ->
            assertEquals("example", context.text)
            assertEquals(Language.ENGLISH, context.source)
            assertEquals(Language.FRENCH, context.target)
            assertEquals(
                listOf(
                    "exemple",
                    "cas",
                    "exemplaire",
                    "modèle",
                    "illustration",
                    "échantillon",
                    "spécimen",
                    "démontrer",
                    "illustrer",
                    "example"
                ), context.translations
            )
            assertEquals(20, context.examples.size)
            assertEquals("Each cited example helped clarify the main idea of the lecture.", context.examples[0].source)
            assertEquals(
                "Chaque exemple cité a aidé à clarifier l'idée principale du cours.",
                context.examples[0].target
            )
        }
    }

    @Test
    fun `test contextOf with language compatibility error`(): Unit = runBlocking {
        val result = reverso.contextOf("example", Language.ENGLISH, Language.ENGLISH)

        assertTrue(result.isLeft())
        result.mapLeft<ReversoError.LanguageCompatibilityError>(::assertIs)
    }

    companion object {

        private val mockClient = HttpClient(MockEngine) {
            install(ContentNegotiation) {
                json()
            }
            Logging {
                level = LogLevel.ALL
            }
            engine {
                addHandler { request ->
                    when (request.url.host) {
                        Reverso.Endpoints().forService(Service.CONTEXT) -> {
                            respond(
                                content = response,
                                headers = headersOf("Content-Type" to listOf(ContentType.Text.Html.toString()))
                            )
                        }
                        else -> error("Unhandled ${request.url.fullPath}")
                    }
                }
            }
        }
        private val reverso = Reverso(Reverso.Endpoints(), mockClient, UrlFactory, HeadersFactory)
    }
}

private val response = """
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>example - Translation into French - examples English | Reverso Context</title>
<meta name="description" content="Translations in context of &quot;example&quot; in English-French from Reverso Context: for example, one example, as an example, another example, good example" />
<link rel="alternate" hreflang="en" href="https://context.reverso.net/translation/english-french/example" /><link rel="alternate" hreflang="fr" href="https://context.reverso.net/traduction/anglais-francais/example" /><link rel="alternate" href="android-app://com.softissimo.reverso.context/reversocontext/context.reverso.net/translation/english-french/example" />
  <meta name="apple-itunes-app" content="app-id=919979642, app-argument=reversocontext://context.reverso.net/translation/english-french/example"/>
<link rel="canonical" href="https://context.reverso.net/translation/english-french/example"/>
<script>
  var request = {
    srctext: "example",
    trgtext: "",
    srclang: "en",
    trglang: "fr",
    rpage: 1,
    rmode: 0,
    original: "example",
    controversialContent: false
  };
  
  var response = {
    srcText: "example",
    trgText: "",
    srcLang: "en",
    trgLang: "fr",
    srcPos: "n.\/v.",
    trgPos: "nm.",
    firstSrcExample: "Each cited <em>example<\/em> helped clarify the main idea of the lecture.",
    firstTrgExample: "Chaque <em>exemple<\/em> cit\u00E9 a aid\u00E9 \u00E0 clarifier l\'id\u00E9e principale du cours.",
    comment: "cas, exemplaire"
  };
  
  const emojisURL = "https://cdn.reverso.net/emojis/svg/{0}.svg";
  const arPromoGIF = "https://cdn.reverso.net/context/v72410/images/ar-translit-promo.gif";
  
  const userLogicCSS = '<link rel="stylesheet preload" type="text/css" media="all" href="https://cdn.reverso.net/context/v72410/css/user_vocab_lists.css" as="style">';
  const voiceLogicCSS = '<link rel="stylesheet preload" type="text/css" media="all" href="https://cdn.reverso.net/context/v72410/css/voice.css" as="style">';
  const userLogicJS = 'https://cdn.reverso.net/context/v72410/js/muserlists.js';
  const voiceLogicJS = 'https://cdn.reverso.net/context/v72410/js/mvoice.js';
  
  const appPromo = {
    "downloadAppLinkDesktop": 'https:\/\/dl.reverso.net\/desktop-app\/macos',
    "detectedOS": 'Mac',
    "appPage": 'https://www.reverso.net/windows-mac-app/en',
    "google_play_link": 'https://play.google.com/store/apps/details?id=com.softissimo.reverso.context&hl=en&referrer=',
    "app_store_link": 'https://apps.apple.com/us/app/reverso-translation-dictionary/id919979642?mt=8&pt=389624'
  }
</script>

<meta name="keywords" content="words in context, translation in context, Reverso Context, idioms in context,  translation search engine, idiomatic translation, idioms translation, translation example, translation examples, Arabic, German, Spanish, French, Hebrew, Italian, Japanese, Korean, Dutch, Polish, Portuguese, Romanian, Russian, Swedish, Turkish, Ukrainian, Chinese, English, idiomatic translations, bilingual concordancer, contextual dictionary, example of use, examples of use, translations in context, context, language lovers, contextual example, idiomatic expressions, dictionary, examples and idioms, concordance tool">
  <meta name="google-play-app" content="app-id=com.softissimo.reverso.context">
  <meta name="google" content="notranslate"/>
<meta name="viewport" content="initial-scale=1.0">
<link rel="preload" href="//cdn.reverso.net/fonts/roboto/latin-n500.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preload" href="//cdn.reverso.net/fonts/robotoc/latin-n400.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preload" href="https://cdn.reverso.net/context/v72410/fonts/contexticons.woff2" as="font" type="font/woff2" crossorigin>
<link rel="preconnect" crossorigin href="https://sdk.privacy-center.org">
<link rel="preconnect" crossorigin href="https://www.googletagmanager.com">
<link rel="preconnect" crossorigin href="https://www.google-analytics.com">
<script defer src="//cdn.reverso.net/abp/v2/Static/JS/advertising.js?ch=1&a=300x250_" onload="abp=true; adblock=false;"></script>
  <script defer src="//cdn.reverso.net/abp/v2/Static/JS/advertising.js?ch=2&a=300x250_" onload="abp=false; adblock=false;"></script>
<script defer src="//cdn.reverso.net/rumjs/conf/rum.config.context.min.js"></script>
<script>
  
  if(typeof window.request === "undefined") {
    window.request = {
      srctext: "",
      trgtext: "",
      srclang: "en",
      trglang: "fr",
      rpage: 1,
      rmode: 0
    };
  }
  var logLOCD = false;
  var context_path = '';
  var special_chars = "،؟י¬¿¡ºª·؛。、・゠＝";
  var is_rtl = false;
  
  var register_link = "https://account.reverso.net/Account/Register?lang=en&utm_source=context&utm_medium={0}&utm_campaign=register&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample";
  var login_link = "https://account.reverso.net/Account/Login?lang=en&utm_source=context&utm_medium={0}&utm_campaign=login&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample";
  
  window.mobilecheck = function() {
    var check = false;
    (function (a) {
      if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4)))
        check = true
      })(navigator.userAgent || navigator.vendor || window.opera);
    return check;
  };
  
  var device = (window.mobilecheck() ? "mobile" : (window.innerWidth>1024 ? "desktop" : "tablet"));
  var environment = 'prod';
</script>
<script>
  var rumjs = rumjs || {};
  rumjs.que = rumjs.que || [];
  
  rumjs.gaque = rumjs.gaque || function(){
    (rumjs.gaque.q=rumjs.gaque.q||[]).push(arguments);
  };
  
  rumjs.gtmque = rumjs.gtmque || function () {
    (rumjs.gtmque.q = rumjs.gtmque.q || []).push(arguments);
  };
  rumjs.gtmque({
    "user_status": logLOCD ? (is_premium ? 'premium-registered' : 'free-registered') : 'free-anonymous',
    "user_id": "",
    "page_category": "Context - Result page",
    "domain": "context",
    "interface_language": "en"
  });
  rumjs.que.push(function () {
    rumjs.config.init();
    rumjs.business.init({});
  });
  
  function sendGTMEvent(paramsMap) {
    rumjs.gtmque(paramsMap);
  }
</script><style class="font-family">
@font-face{font-family:'contexticons';src:url('https://cdn.reverso.net/context/v72410/fonts/contexticons.woff2') format('woff2'),url('https://cdn.reverso.net/context/v72410/fonts/contexticons.ttf') format('truetype'),url('https://cdn.reverso.net/context/v72410/fonts/contexticons.woff') format('woff'),url('https://cdn.reverso.net/context/v72410/fonts/contexticons.svg#contexticons') format('svg');font-weight: normal;font-style: normal;font-display: swap;}

/* cyrillic-ext */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/cyrillicext-n400.woff2') format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/cyrillic-n400.woff2') format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* vietnamese */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/vietnamese-n400.woff2') format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/latinext-n400.woff2') format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/latin-n400.woff2') format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}
/* cyrillic-ext */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/cyrillicext-n500.woff2') format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/cyrillic-n500.woff2') format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* vietnamese */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/vietnamese-n500.woff2') format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/latinext-n500.woff2') format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/latin-n500.woff2') format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}
/* cyrillic-ext */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 600;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/cyrillicext-n600.woff2') format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 600;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/cyrillic-n600.woff2') format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* vietnamese */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 600;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/vietnamese-n600.woff2') format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 600;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/latinext-n600.woff2') format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Montserrat';
  font-style: normal;
  font-weight: 600;
  font-display: swap;
  src: url('//cdn.reverso.net/fonts/montserrat/latin-n600.woff2') format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}
/* cyrillic-ext */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/cyrillicext-n400.woff2) format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/cyrillic-n400.woff2) format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* greek-ext */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/greek-n400.woff2) format('woff2');
  unicode-range: U+1F00-1FFF;
}
/* greek */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/greek-n400.woff2) format('woff2');
  unicode-range: U+0370-03FF;
}
/* vietnamese */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/vietnamese-n400.woff2) format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/latinext-n400.woff2) format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Roboto Condensed';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/robotoc/latin-n400.woff2) format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}

/* cyrillic-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/cyrillicext-n500.woff2) format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/cyrillic-n500.woff2) format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* greek-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/greekext-n500.woff2) format('woff2');
  unicode-range: U+1F00-1FFF;
}
/* greek */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/greek-n500.woff2) format('woff2');
  unicode-range: U+0370-03FF;
}
/* vietnamese */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/vietnamese-n500.woff2) format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/latinext-n500.woff2) format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 500;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/latin-n500.woff2) format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}

/* cyrillic-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/cyrillicext-n700.woff2) format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/cyrillic-n700.woff2) format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* greek-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/greekext-n700.woff2) format('woff2');
  unicode-range: U+1F00-1FFF;
}
/* greek */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/greek-n700.woff2) format('woff2');
  unicode-range: U+0370-03FF;
}
/* vietnamese */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/vietnamese-n700.woff2) format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/latinext-n700.woff2) format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 700;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/latin-n700.woff2) format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}

/* cyrillic-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/cyrillicext-n400.woff2) format('woff2');
  unicode-range: U+0460-052F, U+1C80-1C88, U+20B4, U+2DE0-2DFF, U+A640-A69F, U+FE2E-FE2F;
}
/* cyrillic */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/cyrillic-n400.woff2) format('woff2');
  unicode-range: U+0400-045F, U+0490-0491, U+04B0-04B1, U+2116;
}
/* greek-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/greekext-n400.woff2) format('woff2');
  unicode-range: U+1F00-1FFF;
}
/* greek */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/greek-n400.woff2) format('woff2');
  unicode-range: U+0370-03FF;
}
/* vietnamese */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/vietnamese-n400.woff2) format('woff2');
  unicode-range: U+0102-0103, U+0110-0111, U+0128-0129, U+0168-0169, U+01A0-01A1, U+01AF-01B0, U+1EA0-1EF9, U+20AB;
}
/* latin-ext */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/latinext-n400.woff2) format('woff2');
  unicode-range: U+0100-024F, U+0259, U+1E00-1EFF, U+2020, U+20A0-20AB, U+20AD-20CF, U+2113, U+2C60-2C7F, U+A720-A7FF;
}
/* latin */
@font-face {
  font-family: 'Roboto';
  font-style: normal;
  font-weight: 400;
  font-display: swap;
  src: url(//cdn.reverso.net/fonts/roboto/latin-n400.woff2) format('woff2');
  unicode-range: U+0000-00FF, U+0131, U+0152-0153, U+02BB-02BC, U+02C6, U+02DA, U+02DC, U+2000-206F, U+2074, U+20AC, U+2122, U+2191, U+2193, U+2212, U+2215, U+FEFF, U+FFFD;
}
body {
  margin: 0 auto;
  background-color: #FFFFFF;
  font-family: 'Roboto', 'Noto Sans Arabic', 'Noto Sans Hebrew', 'Tahoma', 'Meiryo', sans-serif;
  font-weight: 400;
  text-align: center;
  min-width: 320px;
  height: 100%;
  transition: 1s ease transform;
}
</style><link rel="stylesheet preload" type="text/css" media="all" href="https://cdn.reverso.net/context/v72410/css/gensearchrca.css" as="style"><link rel="stylesheet preload" type="text/css" media="all" href="https://cdn.reverso.net/context/v72410/css/resultpage.css" as="style"><link rel="stylesheet preload" type="text/css" media="screen and (max-width: 1024px)" href="https://cdn.reverso.net/context/v72410/css/genresponsive.css" as="style"><script defer src="//cdn.reverso.net/ramjs/conf/ram.config.context.min.js"></script>
  <script>
  
    var tagsArray = [device]; // for defineGoogleSlots
    
    if (device !== "mobile") {
    	tagsArray.push('native');
    }
    
    
    var adTargeting = {}
    adTargeting['Interface'] = 'EN';
    adTargeting['device'] = device;
    adTargeting['Status'] = environment;
    
    adTargeting['Direction'] = 'EN-FR';
    
  
    var ramjs = ramjs || {};
    ramjs.que = ramjs.que || [];
    
    ramjs.que.push( function() {
      ramjs.config.init(adTargeting);
      
      ramjs.business.init(tagsArray);
    	if (device === "desktop") {
    		ramjs.que.push(["initdesktop"]);
    	}
    });
  </script><script>
  </script>
<link href="https://cdn.reverso.net/context/v72410/images/reverso180.png" rel="image_src" />
<link rel="apple-touch-icon" href="https://cdn.reverso.net/context/v72410/images/reverso180.png">
<link rel="shortcut icon" href="https://cdn.reverso.net/context/v72410/images/reverso-context.ico">
</head>
  <body class="locked mobile-locked  noticed">
    <div class="loading-pannel">
  <span class="icon"></span>
</div>
<header id="reverso-header-2020">
    <div class="reverso-header-wrapper">
        <div class="reverso-header-left-wrapper">
            <div class="reverso-logo-wrapper">
                <a class="reverso-context-logo" title="Reverso Context: the search engine for translations in context" href="https://context.reverso.net/translation/">
                  <span class="logo rev-logo">
                    <span class="icon reverso-logo"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span><span class="path5"></span><span class="path6"></span><span class="path7"></span><span class="path8"></span><span class="path9"></span><span class="path10"></span></span>
                  </span>
                </a>
            </div>
            <span class="logo-separator"></span>
            <nav class="reverso-links-wrapper">
                <a class="translation-link product" href="https://www.reverso.net/text-translation">
                  <span>Translation</span>
                </a>
                <a class="speller-link product" href="https://www.reverso.net/spell-checker/english-spelling-grammar/">
                  <span>Grammar Check</span>
                </a>
                <a class="context-link product current" href="https://context.reverso.net/translation/">
                  <span>Context</span>
                </a>
                <a class="dictionary-link product" href="https://dictionary.reverso.net/english-definition/" title="Definitions of words and expressions in English, and their translations">
                  <span>Dictionary</span>
                </a>
                <a class="vocabulary-link product" href="https://www.reverso.net/vocabulary">
                  <span>Vocabulary</span>
                </a>
                <div id="more-products-menu" class="selector closed nonselectable">
                    <div class="option front">
                        <span class="icon horizontal-dots"></span>
                    </div>
                    <div class="drop-down">
                        <a class="option vocabulary" href="https://www.reverso.net/vocabulary">
                            <span class="icon flashcards"></span>
                            <span class="text">Vocabulary</span>
                        </a>
                        <a class="option document" href="https://documents.reverso.net/Default.aspx?lang=en&utm_source=reversocontext&utm_medium=textlink&utm_campaign=menu">
                            <span class="icon translate-documents"></span>
                            <span class="text">Documents</span>
                        </a>
                        <a class="option synonyms" href="https://synonyms.reverso.net/synonym/">
                            <span class="icon synonyms"></span>
                            <span class="text">Synonyms</span>
                        </a>
                        <a class="option conjugator" href="https://conjugator.reverso.net/conjugation-english.html">
                            <span class="icon conjugator"></span>
                            <span class="text">Conjugation</span>
                        </a>
                        <a class="option collab-dict" href="https://dictionary.reverso.net/CollabDict.aspx?srcLang=-1&targLang=-1&lang=en">
                            <span class="icon collab-dict"></span>
                            <span class="text">Collaborative Dictionary</span>
                        </a>
                        <a class="option grammar" href="https://grammar.reverso.net/">
                            <span class="icon book"></span>
                            <span class="text">Grammar</span>
                        </a>
                        <a class="option expressio" href="https://www.expressio.fr/">
                            <span class="icon expressio"></span>
                            <span class="text">Expressio</span>
                        </a>
                        <a class="option corporate" href="https://www.corporate-translation.reverso.com/?lang=en">
                            <span class="icon briefcase"></span>
                            <span class="text">Reverso Corporate</span>
                        </a>
                    </div>
                </div>
            </nav>
            <div id="mobile-info-menu" class="selector closed nonselectable">
              <div class="option front">
                <span class="text">Context</span>
                <span class="sel-arrow icon down-arrow"></span>
              </div>
              <div class="floating-menu drop-down">
                <a aria-label="Download our <b>free</b> app" class="option mobileapp grey-item" href="/translation/mobile-app?utm_source=context&utm_medium=user-menu-header&utm_content=ctxtappdl" data-os="android">
                  <span class="mapp-title">
                    <span class="mapp-icon">
                      <span class="icon reverso-icon"><span class="path1"></span><span class="path2"></span><span class="path3"></span></span>
                    </span>
                    <span class="text">Download our free app</span>
                  </span>
                  <span class="badge and no-loading en"></span>
                  </a>
                <div class="sep-full"></div>
                <a class="option translation sep-24" href="https://www.reverso.net/text-translation">
                    <span class="icon lang"></span>
                    <span class="text">Translation</span>
                </a>
                <a class="option speller sep-24" href="https://www.reverso.net/spell-checker/english-spelling-grammar/">
                    <span class="icon spellcheck"></span>
                    <span class="text">Grammar Check</span>
                </a>
                <a class="option context sep-24" href="https://context.reverso.net/translation/">
                    <span class="icon search"></span>
                    <span class="text">Context</span>
                    <span class="icon tick2"></span>
                </a>
                <a class="option dictionary sep-24" href="https://dictionary.reverso.net">
                    <span class="icon dictionary"></span>
                    <span class="text">Dictionary</span>
                </a>
                <a class="option vocabulary sep-24" href="https://www.reverso.net/vocabulary">
                    <span class="icon flashcards"></span>
                    <span class="text">Vocabulary</span>
                </a>
                <a class="option document sep-24" href="https://documents.reverso.net/Default.aspx?lang=en&utm_source=reversocontext&utm_medium=textlink&utm_campaign=menu">
                    <span class="icon translate-documents"></span>
                    <span class="text">Documents</span>
                </a>
                <a class="option synonyms sep-24" href="https://synonyms.reverso.net/synonym/">
                    <span class="icon synonyms"></span>
                    <span class="text">Synonyms</span>
                </a>
                <a class="option conjugator sep-24" href="https://conjugator.reverso.net/conjugation-english.html">
                    <span class="icon conjugator"></span>
                    <span class="text">Conjugation</span>
                </a>
                <a class="option collab-dict sep-24 hidden" href="https://dictionary.reverso.net/CollabDict.aspx?srcLang=-1&targLang=-1&lang=en">
                    <span class="icon collab-dict"></span>
                    <span class="text">Collaborative Dictionary</span>
                </a>
                <a class="option grammar sep-24 hidden" href="https://grammar.reverso.net/">
                    <span class="icon book"></span>
                    <span class="text">Grammar</span>
                </a>
                <a class="option expressio sep-24 hidden" href="https://www.expressio.fr/">
                    <span class="icon expressio"></span>
                    <span class="text">Expressio</span>
                </a>
                <a class="option corporate hidden" href="https://www.corporate-translation.reverso.com/?lang=en">
                    <span class="icon briefcase"></span>
                    <span class="text">Reverso Corporate</span>
                </a>
                <button class="option more-menu">More<span class="icon down-arrow"></span></button>
                <div class="sep-full"></div>
                <div class="space-filler"></div>
              </div>
            </div>
        </div>
        <div class="reverso-header-right-wrapper">
            <a class="dapp-dl-button " data-os="mac" data-dl="https://www.reverso.net/windows-mac-app/en/thanks?utm_source=context&utm_campaign=headerbutton" title="Download for free<span class='newlink'>Translate text from any application or website in just one click.</span>" href="https://dl.reverso.net/desktop-app/macos?utm_source=context&utm_campaign=headerbutton">
                  <span class="icon download"></span>
                  <span class="text">Download for Mac</span>
                </a>
            <div id="reverso-user-menu" class="reverso-user-menu selector closed nonselectable  ">
    <div class="option front">
  		<span class="icon user"></span>
        <span class="login">Log in</span>
      <span class="icon cancel"></span>
    </div>
    <div class="drop-down">
  		<a class="option register sep-24" href="https://account.reverso.net/Account/Register?lang=en&utm_source=context&utm_medium=user-menu-header&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample">
            <span class="icon add-user"></span>
            <span class="text">Register</span>
        </a>
        <a class="option login sep-24" href="https://account.reverso.net/Account/Login?lang=en&utm_source=context&utm_medium=user-menu-header&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample">
            <span class="icon login"></span>
            <span class="text">Log in</span>
        </a>
        <span id="fb-content" class="option facebook sep-24">
            <span class="icon facebook"><span class="path1"></span><span class="path2"></span></span>
            <span class="text">Connect with Facebook</span>
        </span>
        <span id="google-content" class="option google sep-24">
            <span class="icon google"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span></span>
            <span class="text">Connect with Google</span>
        </span>
        <span id="apple-content" class="option apple sep-24">
            <span class="icon apple"></span>
            <span class="text">Connect with Apple</span>
        </span>
        <div class="sep-full mobile-hidden"></div>
        <a class="option premium" href="https://documents.reverso.net/Pricing.aspx?lang=en&origin=1">
            <span class="icon crown"></span>
            <span class="text">Reverso Premium</span>
        </a>
      <div class="sep-full"></div>
      <div class="interface-langs-select grey-item">
          <span class="icon web"></span>
          <select>
            <option class="option"  value="https://context.reverso.net/%D8%A7%D9%84%D8%AA%D8%B1%D8%AC%D9%85%D8%A9/" data-value="ar">العربية</option>
            <option class="option"  value="https://context.reverso.net/%C3%BCbersetzung/" data-value="de">Deutsch</option>
            <option class="option" selected value="https://context.reverso.net/translation/" data-value="en">English</option>
            <option class="option"  value="https://context.reverso.net/traduccion/" data-value="es">Español</option>
            <option class="option"  value="https://context.reverso.net/traduction/" data-value="fr">Français</option>
            <option class="option"  value="https://context.reverso.net/%D7%AA%D7%A8%D7%92%D7%95%D7%9D/" data-value="he">עברית</option>
            <option class="option"  value="https://context.reverso.net/traduzione/" data-value="it">Italiano</option>
            <option class="option"  value="https://context.reverso.net/%E7%BF%BB%E8%A8%B3/" data-value="ja">日本語</option>
            <option class="option"  value="https://context.reverso.net/vertaling/" data-value="nl">Nederlands</option>
            <option class="option"  value="https://context.reverso.net/t%C5%82umaczenie/" data-value="pl">Polski</option>
            <option class="option"  value="https://context.reverso.net/traducao/" data-value="pt">Português</option>
            <option class="option"  value="https://context.reverso.net/traducere/" data-value="ro">Română</option>
            <option class="option"  value="https://context.reverso.net/%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/" data-value="ru">Русский</option>
            <option class="option"  value="https://context.reverso.net/%C3%B6vers%C3%A4ttning/" data-value="sv">Svenska</option>
            <option class="option"  value="https://context.reverso.net/%C3%A7eviri/" data-value="tr">Türkçe</option>
            <option class="option"  value="https://context.reverso.net/%D0%BF%D0%B5%D1%80%D0%B5%D0%BA%D0%BB%D0%B0%D0%B4/" data-value="uk">Українська</option>
            <option class="option"  value="https://context.reverso.net/%E7%BF%BB%E8%AF%91/" data-value="zh">中文</option>
            </select>
      </div>
      <div class="sep-full"></div>
      <div class="space-filler"></div>
    </div>
</div><div id="interface-lang-menu" class="selector closed nonselectable">
              <div class="option-wrapper">
                <div class="option front">
                    <span class="text">en</span>
                </div>
                <span class="sel-arrow icon down-caret-full"></span>
              </div>
              <div class="drop-down">
                <a href="https://context.reverso.net/%D8%A7%D9%84%D8%AA%D8%B1%D8%AC%D9%85%D8%A9/" class="option" data-value="ar">العربية</a>
                <a href="https://context.reverso.net/%C3%BCbersetzung/" class="option" data-value="de">Deutsch</a>
                <a href="https://context.reverso.net/translation/" class="option selected" data-value="en">English</a>
                <a href="https://context.reverso.net/traduccion/" class="option" data-value="es">Español</a>
                <a href="https://context.reverso.net/traduction/" class="option" data-value="fr">Français</a>
                <a href="https://context.reverso.net/%D7%AA%D7%A8%D7%92%D7%95%D7%9D/" class="option" data-value="he">עברית</a>
                <a href="https://context.reverso.net/traduzione/" class="option" data-value="it">Italiano</a>
                <a href="https://context.reverso.net/%E7%BF%BB%E8%A8%B3/" class="option" data-value="ja">日本語</a>
                <a href="https://context.reverso.net/vertaling/" class="option" data-value="nl">Nederlands</a>
                <a href="https://context.reverso.net/t%C5%82umaczenie/" class="option" data-value="pl">Polski</a>
                <a href="https://context.reverso.net/traducao/" class="option" data-value="pt">Português</a>
                <a href="https://context.reverso.net/traducere/" class="option" data-value="ro">Română</a>
                <a href="https://context.reverso.net/%D0%BF%D0%B5%D1%80%D0%B5%D0%B2%D0%BE%D0%B4/" class="option" data-value="ru">Русский</a>
                <a href="https://context.reverso.net/%C3%B6vers%C3%A4ttning/" class="option" data-value="sv">Svenska</a>
                <a href="https://context.reverso.net/%C3%A7eviri/" class="option" data-value="tr">Türkçe</a>
                <a href="https://context.reverso.net/%D0%BF%D0%B5%D1%80%D0%B5%D0%BA%D0%BB%D0%B0%D0%B4/" class="option" data-value="uk">Українська</a>
                <a href="https://context.reverso.net/%E7%BF%BB%E8%AF%91/" class="option" data-value="zh">中文</a>
                </div>
            </div>
        </div>
    </div>
</header>
<div class="background-top">
      <div id="top-content" class="wrapper-width">
        <div id="search-content-wrapper">
          <section id="search-content">
  <div id="search">
    <div id="search-input" class="ltr">
      <input tabindex="0" type="text" name="text" id="entry" enterkeyhint="search" value="example" autocapitalize="none" autocomplete="off" class="keyboardInput ui-autocomplete-input ltr" placeholder="Enter a word, expression or long text"/>
      <div class="icons">
        <button class="icon exclude" title="Click here to exclude some words from your search. For instance, word1 -{word2} : will search phrases that contain word1 and NOT word2"></button>
        <button class="icon cancel" title="Clear this input"></button>
        <div class="separator"></div>
        <a href="https://documents.reverso.net/Default.aspx?lang=en&utm_source=contextweb&utm_campaign=button-searchbar-result" class="blue translate-file-search icon translate-file" title="Translate entire documents (.docx, .pptx, .pdf,…) while keeping their original layout" aria-label="Translate a file"></a>
        <button id="search-button" class="blue icon search" title="Search" tabindex="0"></button>
      </div>
    </div>
    <div id="pair-selector" class="nonselectable">
      <div id="src-selector" class="selector double closed has-extra-langs" tabindex="0">
        <div class="option-wrapper">
          <span class="option front" data-value="en">
            <span class="lang-name">English</span>
          </span>
          <span class="sel-arrow icon down-arrow"></span>
        </div>
        <div class="drop-down">
          <div class="languages">
            <span class="option" data-value="ar">
                <span class="lang-name">Arabic</span>
              </span>
            <span class="option" data-value="de">
                <span class="lang-name">German</span>
              </span>
            <span class="option selected" data-value="en">
                <span class="lang-name">English</span>
              </span>
            <span class="option" data-value="es">
                <span class="lang-name">Spanish</span>
              </span>
            <span class="option" data-value="fr">
                <span class="lang-name">French</span>
              </span>
            <span class="option" data-value="he">
                <span class="lang-name">Hebrew</span>
              </span>
            <span class="option" data-value="it">
                <span class="lang-name">Italian</span>
              </span>
            <span class="option" data-value="ja">
                <span class="lang-name">Japanese</span>
              </span>
            <span class="option" data-value="ko">
                <span class="lang-name">Korean</span>
              </span>
            <span class="option" data-value="nl">
                <span class="lang-name">Dutch</span>
              </span>
            <span class="option" data-value="pl">
                <span class="lang-name">Polish</span>
              </span>
            <span class="option" data-value="pt">
                <span class="lang-name">Portuguese</span>
              </span>
            <span class="option" data-value="ro">
                <span class="lang-name">Romanian</span>
              </span>
            <span class="option" data-value="ru">
                <span class="lang-name">Russian</span>
              </span>
            <span class="option" data-value="sv">
                <span class="lang-name">Swedish</span>
              </span>
            <span class="option" data-value="tr">
                <span class="lang-name">Turkish</span>
              </span>
            <span class="option" data-value="uk">
                <span class="lang-name">Ukrainian</span>
              </span>
            <span class="option" data-value="zh">
                <span class="lang-name">Chinese</span>
              </span>
            </div>
          <div class="extra-langs">
            <div class="extra-langs-title">
              <span class="show-more">Show more</span>
              <span class="langs">(Greek, Hindi, Thai, Czech...)</span>
            </div>
            <div class="extra-langs-content">
            <span class="option external" data-value="cs">
                  <span class="lang-name">Czech</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="da">
                  <span class="lang-name">Danish</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="el">
                  <span class="lang-name">Greek</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="fa">
                  <span class="lang-name">Persian</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="hi">
                  <span class="lang-name">Hindi</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="hu">
                  <span class="lang-name">Hungarian</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="sk">
                  <span class="lang-name">Slovak</span>
                  <span class="icon launch"></span>
                </span>              <span class="option external" data-value="th">
                  <span class="lang-name">Thai</span>
                  <span class="icon launch"></span>
                </span>              </div>
            <div class="extra-langs-less">Show less</div>
          </div>
        </div>
      </div>
      <div class="swap-content" title="Change the translation direction">
        <span class="icon swap"></span>
      </div>
      <div id="trg-selector" class="selector double closed has-extra-langs" tabindex="0">
        <div class="option-wrapper">
          <span class="option  front" data-value="fr">
            <span class="lang-name">French</span>
          </span>
          <span class="sel-arrow icon down-arrow"></span>
        </div>
        <div class="drop-down">
          <div class="languages">
            <span class="option" data-value="definition">
              <span class="icon dictionary"></span>
              <span class="lang-name">Definition</span>
            </span>
            <span class="option" data-value="ar">
                <span class="lang-name">Arabic</span>
              </span>
            <span class="option" data-value="de">
                <span class="lang-name">German</span>
              </span>
            <span class="option" data-value="en">
                <span class="lang-name">English</span>
              </span>
            <span class="option" data-value="es">
                <span class="lang-name">Spanish</span>
              </span>
            <span class="option selected" data-value="fr">
                <span class="lang-name">French</span>
              </span>
            <span class="option" data-value="he">
                <span class="lang-name">Hebrew</span>
              </span>
            <span class="option" data-value="it">
                <span class="lang-name">Italian</span>
              </span>
            <span class="option" data-value="ja">
                <span class="lang-name">Japanese</span>
              </span>
            <span class="option" data-value="ko">
                <span class="lang-name">Korean</span>
              </span>
            <span class="option" data-value="nl">
                <span class="lang-name">Dutch</span>
              </span>
            <span class="option" data-value="pl">
                <span class="lang-name">Polish</span>
              </span>
            <span class="option" data-value="pt">
                <span class="lang-name">Portuguese</span>
              </span>
            <span class="option" data-value="ro">
                <span class="lang-name">Romanian</span>
              </span>
            <span class="option" data-value="ru">
                <span class="lang-name">Russian</span>
              </span>
            <span class="option" data-value="sv">
                <span class="lang-name">Swedish</span>
              </span>
            <span class="option" data-value="tr">
                <span class="lang-name">Turkish</span>
              </span>
            <span class="option" data-value="uk">
                <span class="lang-name">Ukrainian</span>
              </span>
            <span class="option" data-value="zh">
                <span class="lang-name">Chinese</span>
              </span>
            </div>
          <div class="extra-langs">
            <div class="extra-langs-title">
              <span class="show-more">Show more</span>
              <span class="langs"></span>
            </div>
            <div class="extra-langs-content">
            <span class="option external" data-value="cs">
                <span class="lang-name">Czech</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="da">
                <span class="lang-name">Danish</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="el">
                <span class="lang-name">Greek</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="fa">
                <span class="lang-name">Persian</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="hi">
                <span class="lang-name">Hindi</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="hu">
                <span class="lang-name">Hungarian</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="sk">
                <span class="lang-name">Slovak</span>
                <span class="icon launch"></span>
              </span>
              <span class="option external" data-value="th">
                <span class="lang-name">Thai</span>
                <span class="icon launch"></span>
              </span>
              </div>
            <div class="extra-langs-less">Show less</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="exclude-content">
    <div class="input-content">
      <input placeholder="Exclude from search">
    </div>
    <button class="icon add disabled"></button>
  </div>
</section><section id="notice-content">
  <div id="dym-content" class="notice suggested search  mobile-hidden" data-pair="null">
    <span class="text">Suggestions:</span>
    <a class="dym-link seealso-link mobile-hidden" href="/translation/english-french/for+example">for example</a>
        <a class="dym-link seealso-link mobile-hidden" href="/translation/english-french/as+an+example">as an example</a>
        <a class="dym-link seealso-link mobile-hidden" href="/translation/english-french/one+example">one example</a>
        <a class="dym-link seealso-link mobile-hidden" href="/translation/english-french/good+example">good example</a>
        <a class="dym-link seealso-link mobile-hidden" href="/translation/english-french/another+example">another example</a>
        </div>
  </section></div>
        <section id="subtop-content">
  <div class="user-profile">
    <a href="https://documents.reverso.net/Pricing.aspx?lang=en&origin=1" class="premium-top rca-no-ads"><span class="icon crown-unfilled"></span><span>Premium</span></a>
    <a href="https://www.reverso.net/vocabulary" class="button favourites" title="My Vocabulary">
      <span class="icon non-favourite"></span>
    </a>
    <a href="https://www.reverso.net/history/en" class="button history" title="See the whole history">
      <span class="icon history"></span>
    </a>
    <a class="button info" href="https://context.reverso.net/translation/about" title="About Reverso Context">
      <span class="icon help_outline"></span>
    </a>
  </div>
  </section>
</div>
    </div>
    <div id="wrapper" class="wrapper-width results">
      <section id="body-content">
  <div class="right-content">
    <div id="rca">
  <span class="rca-title">Advertising</span>
  <div id='div-gpt-ad-Reverso_Ctxt_D_Result_300x250_ATF' class="rca">
    <script>
      if(!window.mobilecheck()) {
        ramjs.que.push(function() { ramjs.business.displayGoogletagSlot('div-gpt-ad-Reverso_Ctxt_D_Result_300x250_ATF') });
      }
    </script>
  </div>
  <div id='div-gpt-ad-Reverso_Ctxt_D_Result_300x250_BTF' class="rca">
    <script>
      if(!window.mobilecheck()) {
        ramjs.que.push(function() { ramjs.business.displayGoogletagSlot('div-gpt-ad-Reverso_Ctxt_D_Result_300x250_BTF') });
      }
    </script>
  </div>
  <div id='div-gpt-ad-Reverso_Ctxt_D_Result_300x600_BTF' class="rca disabled tall-rca">
      <script>
        if(!window.mobilecheck()) {
          ramjs.que.push(function() { ramjs.business.displayGoogletagSlot('div-gpt-ad-Reverso_Ctxt_D_Result_300x600_BTF') });
        }
      </script>
    </div>
  <a href="https://documents.reverso.net/Pricing.aspx?lang=en&origin=1" class="rca-no-ads hidden">No ads with Premium</a>
</div>
</div>
  <div class="left-content">
    <div id="register-popup-bottom" class="popup closed">
  <div class="header">
    <span class="title">Join Reverso, it's <b>free and fast</b>!</span>
    <button class="icon up-arrow" title="Close"></button>
  </div>
  <div class="body wide-container">
    <a class="button register" href="https://account.reverso.net/Account/Register?lang=en&utm_source=context&utm_medium=popup-static-left-bottom&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample"><span class="icon add-user"></span>Register</a>
    <a class="button login" href="https://account.reverso.net/Account/Login?lang=en&utm_source=context&utm_medium=popup-static-left-bottom&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample"><span class="icon login"></span>Log in</a>
  </div>
</div>
<section id="search-top-content" class=" transliteration">
  <div id="search-options" class=" en-source">
    <div class="title-content">
      <span class="search-text">example</span>
      <div class="title-icons">
        <div class="voice-variants">
            <button class="voice-variant" title="Pronunciation">
              <img src="https://cdn.reverso.net/context/v72410/images/flags/en-us.svg" class="flag en-us"/>
              <span data-lang="en-US" class="voice icon stopped search-option"></span>
            </button>
            <button class="voice-variant" title="Pronunciation">
              <img src="https://cdn.reverso.net/context/v72410/images/flags/en-uk.svg" class="flag en-en"/>
              <span data-lang="en-UK" class="voice icon stopped search-option"></span>
            </button>
          </div>
          <button class="search-option save-fav show-fav-lists-initiator non-favourite"><span class="icon non-favourite"></span><span class="text">Add to list</span></button>
      </div>
      <div id="transliteration-content" class="loaded">
            <div class="message">
            <div title="<div>IPA Pronunciation</div>"class="ipa">/ɪɡ'zæmpəl/</div>
            <div title="<div>IPA Pronunciation</div>"class="ipa">/ɪɡ'zɑ&#720; m.pəl/</div>
            </div>
          </div>
        </div>
  </div>
  <div id="search-links">
    <div id="search-links-content">
      <h1>Translation of &quot;example&quot; in French</h1>
      <a draggable="false" title="English meanings and usage examples for &quot;example&quot;" class="definition search-option" data-value="definition" href="https://dictionary.reverso.net/english-definition/example#french">
          <span class="text">Definition</span>
          </a>
      <a draggable="false" title="Synonyms and analogies of &quot;example&quot; in English" class="synonyms search-option" data-value="synonyms" href="https://synonyms.reverso.net/synonym/en/example">Synonyms</a>
      <a draggable="false" title="Conjugate the verb &quot;example&quot; in all moods and tenses" class="conjugator search-option" data-value="conjugator" href="https://conjugator.reverso.net/conjugation-english-verb-example.html">Conjugation</a>
      <button title="More actions" class="search-option more-options icon horizontal-dots"></button>
      <div class="search-options-menu closed">
        <button data-value="images" data-url="https://www.google.com/search?hl=en&tbm=isch&q=" title="<span class='newlink'><span class='icon launch'></span>This link will open in a new tab</span>"><span>Search in Images</span><span class="icon images"></span></button>
        <button data-value="wikipedia" data-url="https://en.wikipedia.org/wiki/" title="<span class='newlink'><span class='icon launch'></span>This link will open in a new tab</span>"><span>Search in Wikipedia</span><span class="icon wikipedia"></span></button>
        <button data-value="web" data-url="https://www.google.com/search?hl=en&q=" title="<span class='newlink'><span class='icon launch'></span>This link will open in a new tab</span>"><span>Search in Web</span><span class="icon web"></span></button>
      </div>
    </div>
  </div>
</section>
<section id="top-results">
  <div id="pos-filters">

      <button class="n" data-pos="n." data-index="0" title="Display all translations tagged as Noun">
        Noun</button>
      <button class="v" data-pos="v." data-index="1" title="Display all translations tagged as Verb">
        Verb</button>
      </div> <div id="filtered-entry" class="wide-container">
    </div>
    <div id="translations-content" class="wide-container">
    <a href="/translation/french-english/exemple" class="translation ltr dict n" data-original-pos='nm.' data-pos='[nm]' data-inflected='exemples' data-pos-index='0' data-term="exemple" data-posGroup="1" data-freq="712725" title="<div class='nobold'><em class='translation'>exemple</em><br>Noun - Masculine<br><br>Click to view +10k examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Masculine"></span>
                  </div>
          <span class="display-term">exemple</span> <span class="gender">m</span></a>
        <a href="/translation/french-english/cas" class="translation ltr dict n" data-original-pos='nm.' data-pos='[nm]' data-pos-index='0' data-term="cas" data-posGroup="1" data-freq="2644" title="<div class='nobold'><em class='translation'>cas</em><br>Noun - Masculine<br><br>Click to view 2644 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Masculine"></span>
                  </div>
          <span class="display-term">cas</span> <span class="gender">m</span></a>
        <a href="/translation/french-english/exemplaire" class="translation ltr dict n" data-original-pos='nm.' data-pos='[nm]' data-inflected='exemplaires' data-pos-index='0' data-term="exemplaire" data-posGroup="1" data-freq="2031" title="<div class='nobold'><em class='translation'>exemplaire</em><br>Noun - Masculine<br><br>Click to view 2031 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Masculine"></span>
                  </div>
          <span class="display-term">exemplaire</span> <span class="gender">m</span></a>
        <a href="/translation/french-english/mod%C3%A8le" class="translation ltr dict n" data-original-pos='nm.' data-pos='[nm]' data-pos-index='0' data-term="modèle" data-posGroup="1" data-freq="1884" title="<div class='nobold'><em class='translation'>modèle</em><br>Noun - Masculine<br><br>Click to view 1884 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Masculine"></span>
                  </div>
          <span class="display-term">modèle</span> <span class="gender">m</span></a>
        <a href="/translation/french-english/illustration" class="translation ltr dict n" data-original-pos='nf.' data-pos='[nf]' data-pos-index='0' data-term="illustration" data-posGroup="1" data-freq="1524" title="<div class='nobold'><em class='translation'>illustration</em><br>Noun - Feminine<br><br>Click to view 1524 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Feminine"></span>
                  </div>
          <span class="display-term">illustration</span> <span class="gender">f</span></a>
        <a href="/translation/french-english/%C3%A9chantillon" class="translation ltr dict n" data-original-pos='nm.' data-pos='[nm]' data-pos-index='0' data-term="échantillon" data-posGroup="1" data-freq="126" title="<div class='nobold'><em class='translation'>échantillon</em><br>Noun - Masculine<br><br>Click to view 126 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Masculine"></span>
                  </div>
          <span class="display-term">échantillon</span> <span class="gender">m</span></a>
        <div class="transGroup new-group"><a href="/translation/french-english/sp%C3%A9cimen" class="translation ltr dict n" data-original-pos='nm.' data-pos='[nm]' data-pos-index='0' data-term="spécimen" data-posGroup="1" data-freq="113" title="<div class='nobold'><em class='translation'>spécimen</em><br>Noun - Masculine<br><br>Click to view 113 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="n" title="Noun - Masculine"></span>
                  </div>
          <span class="display-term">spécimen</span> <span class="gender">m</span></a>
        </div>
      <div class="transGroup new-group"><a href="/translation/french-english/d%C3%A9montrer" class="translation ltr dict v" data-original-pos='v.' data-pos='[v]' data-pos-index='1' data-term="démontrer" data-posGroup="2" data-freq="36" title="<div class='nobold'><em class='translation'>démontrer</em><br>Verb<br><br>Click to view 36 examples</div>" lang='fr'>
          <div class="pos-mark">
              <span class="v" title="Verb"></span>
                  </div>
          <span class="display-term">démontrer</span></a>
        </div>
      <a href="/translation/french-english/illustrer" class="translation ltr dict mobile-trans-hidden translation-hidden no-pos" data-inflected='illustre}--{illustrent' data-term="illustrer" data-posGroup="6" data-freq="2222" title="<div class='nobold'><em class='translation'>illustrer</em><br><br>Click to view 2222 examples</div>" lang='fr'>
          <span class="display-term">illustrer</span></a>
        <a href="/translation/french-english/example" class="translation ltr dict mobile-trans-hidden translation-hidden no-pos" data-term="example" data-posGroup="6" data-freq="1596" title="<div class='nobold'><em class='translation'>example</em><br><br>Click to view 1596 examples</div>" lang='fr'>
          <span class="display-term">example</span></a>
        <button class="more-button" title="Discover more translations and suggestions when available">Show more</button>
      <button title="Other translations" class="other-content" data-other="0" data-negative=" -{exemple} -{exemples} -{cas} -{exemplaire} -{exemplaires} -{mod&egrave;le} -{illustration} -{&eacute;chantillon} -{sp&eacute;cimen} -{d&eacute;montrer} -{illustrer} -{illustre} -{illustrent} -{example}">[...]</button>
    </div>
  <section id="filters-content" class="wide-container">
    <div class="filter-form closed">
      <button class="icon filter" title="You can obtain even more precise results by indicating the word(s) that the translation should have. This will allow you to show only the examples that contain those words."></button>
      <button class="icon cancel" title="Clear filter"></button>
      <div class="input-content">
        <button class="icon exclude" title="Click here to exclude some words from your search. For instance, word1 -{word2} : will search phrases that contain word1 and NOT word2"></button>
        <input placeholder="Display the translations containing..." autocomplete="off" readonly>
      </div>
    </div>
  </section>
  <section id="top-suggestions" class="wide-container">
    <h3>Suggestions</h3>
    <div class="suggestions-content">
      <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/for+example">for example</button>
            <span class="figure ">+10k</span>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/one+example">one example</button>
            <span class="figure ">8430</span>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/as+an+example">as an example</button>
            <span class="figure ">5782</span>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/another+example">another example</button>
            <span class="figure ">4797</span>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/good+example">good example</button>
            <span class="figure ">4502</span>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/set+an+example">set an example</button>
            <span class="figure ">1368</span>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/perfect+example">perfect example</button>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/by+example">by example</button>
            </div>
        <div class="suggestion negative" title="This suggestion is excluded from the results">
            <span class="icon pf"></span>
            <button class="text" data-url="/translation/english-french/great+example">great example</button>
            </div>
        <button class="icon other other-suggestions" title="Other translations" data-negative="-{for example} -{one example} -{as an example} -{another example} -{good example} -{set an example} -{perfect example} -{by example} -{great example}"></button>
    </div>
  </section>
  <button class="less-button">Show less</button>
  </section>
<aside class="rudeness-messages">
  <div id="rude" class="notice">
    <i class="icon warning"></i>
    <span class="message">Potentially sensitive or inappropriate examples</span>
  </div>
  <div id="colloquial" class="notice">
    <i class="icon warning"></i>
    <span class="message">These examples may contain colloquial words based on your search.</span>
  </div>
</aside>
<section id="examples-content" class="wide-container ">
  <div id="CONTEXT-SOURCE2.EN-FR_272757" class="example">
      <div class="src ltr">
        <span class="text">
          Each cited <em>example</em> helped clarify the main idea of the lecture.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Chaque <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> cité a aidé à clarifier l'idée principale du cours.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_272757" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_272757" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_272757"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_272757"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_272757" data-text="Chaque <em>exemple</em> cité a aidé à clarifier l'idée principale du cours."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_272757"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_272757"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_276139" class="example">
      <div class="src ltr">
        <span class="text">
          The book is a classic <em>example</em> of literature from that era.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Ce livre est un parfait <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> de la littérature de cette époque.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_276139" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_276139" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_276139"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_276139"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_276139" data-text="Ce livre est un parfait <em>exemple</em> de la littérature de cette époque."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_276139"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_276139"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1336772" class="example">
      <div class="src ltr">
        <span class="text">
          Her dedication to helping others is a shining <em>example</em> of true compassion.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Son dévouement à aider les autres est un modèle <a class="link_highlighted" href='/translation/french-english/exemplaire' rel="nofollow"><em>exemplaire</em></a> de véritable compassion.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1336772" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1336772" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1336772"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1336772"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1336772" data-text="Son dévouement à aider les autres est un modèle <em>exemplaire</em> de véritable compassion."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1336772"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1336772"></button>
          </div>
        </div>
      </div>
    </div>
  <div id='lig_reverso_smartbox_article_tc' class="inner-rca unloaded" data-position="4">
	        <!-- /2629866/Context_1x1_Banner -->
	        <div id='div-gpt-ad-Reverso_Ctxt_R_Result_InFeed_ATF'>
	          <script>ramjs.que.push(function() { ramjs.business.displayGoogletagSlot('div-gpt-ad-Reverso_Ctxt_R_Result_InFeed_ATF') });</script>
	        </div>
	      </div>
        <div id="CONTEXT-SOURCE2.EN-FR_1212061" class="example">
      <div class="src ltr">
        <span class="text">
          He gave a real-life <em>example</em> of how to solve that problem.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Il a donné un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> concret de la façon de résoudre ce problème.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1212061" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1212061" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1212061"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1212061"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1212061" data-text="Il a donné un <em>exemple</em> concret de la façon de résoudre ce problème."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1212061"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1212061"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1594913" class="example">
      <div class="src ltr">
        <span class="text">
          Their story is a beautiful <em>example</em> of true love overcoming obstacles.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Leur histoire est un bel <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> d'amour profond surmontant tous les obstacles.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1594913" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1594913" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1594913"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1594913"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1594913" data-text="Leur histoire est un bel <em>exemple</em> d'amour profond surmontant tous les obstacles."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1594913"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1594913"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1531584" class="example">
      <div class="src ltr">
        <span class="text">
          His tactics in the game are a textbook <em>example</em> of good strategy.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Ses tactiques dans le jeu sont un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> classique de bonne stratégie.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1531584" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1531584" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1531584"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1531584"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1531584" data-text="Ses tactiques dans le jeu sont un <em>exemple</em> classique de bonne stratégie."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1531584"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1531584"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="IDIOMS.EN-FR_129897" class="example">
      <div class="src ltr">
        <span class="text">
          The court will make an <em>example</em> of drunk drivers to deter future offenses.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Le tribunal fera un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> des conducteurs ivres pour dissuader de futures infractions.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="IDIOMS.EN-FR_129897" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="IDIOMS.EN-FR_129897" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="IDIOMS.EN-FR_129897"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="IDIOMS.EN-FR_129897"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="IDIOMS.EN-FR_129897" data-text="Le tribunal fera un <em>exemple</em> des conducteurs ivres pour dissuader de futures infractions."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="IDIOMS.EN-FR_129897"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="IDIOMS.EN-FR_129897"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1857548" class="example">
      <div class="src ltr">
        <span class="text">
          His dedication was so admirable that others wanted to follow his <em>example</em>.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Son dévouement était si admirable que les autres voulaient suivre son <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a>.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1857548" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1857548" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1857548"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1857548"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1857548" data-text="Son dévouement était si admirable que les autres voulaient suivre son <em>exemple</em>."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1857548"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1857548"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_218611" class="example">
      <div class="src ltr">
        <span class="text">
          By way of <em>example</em>, consider the first case study.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          À titre d'<a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a>, considérons la première étude de cas.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_218611" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_218611" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_218611"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_218611"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_218611" data-text="À titre d'<em>exemple</em>, considérons la première étude de cas."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_218611"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_218611"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1328599" class="example blocked">
      <div class="src ltr">
        <span class="text">
          Her refusal to shame others for their decisions set a positive <em>example</em>.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Son refus de juger les autres pour leurs décisions a donné un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> positif.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1328599" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1328599" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1328599"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1328599"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1328599" data-text="Son refus de juger les autres pour leurs décisions a donné un <em>exemple</em> positif."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1328599"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1328599"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1531565" class="example blocked">
      <div class="src ltr">
        <span class="text">
          Her painting is a textbook <em>example</em> of modern art that everyone admires.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Son tableau est un parfait <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> d'art moderne que tout le monde admire.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1531565" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1531565" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1531565"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1531565"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1531565" data-text="Son tableau est un parfait <em>exemple</em> d'art moderne que tout le monde admire."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1531565"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1531565"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1161830" class="example blocked">
      <div class="src ltr">
        <span class="text">
          The book serves as a prime <em>example</em> of modern literature's evolution.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Le livre sert d'<a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> emblématique de l'évolution de la littérature moderne.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1161830" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1161830" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1161830"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1161830"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1161830" data-text="Le livre sert d'<em>exemple</em> emblématique de l'évolution de la littérature moderne."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1161830"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1161830"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1161820" class="example blocked">
      <div class="src ltr">
        <span class="text">
          This project is a prime <em>example</em> of sustainable practices in urban planning.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Ce projet est un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> parfait de pratiques durables en urbanisme.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1161820" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1161820" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1161820"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1161820"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1161820" data-text="Ce projet est un <em>exemple</em> parfait de pratiques durables en urbanisme."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1161820"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1161820"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="IDIOMS.EN-FR_129896" class="example blocked">
      <div class="src ltr">
        <span class="text">
          The boss wanted to make an <em>example</em> of employees who consistently arrived late to work.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Le patron voulait faire un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> des employés qui arrivaient constamment en retard au travail.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="IDIOMS.EN-FR_129896" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="IDIOMS.EN-FR_129896" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="IDIOMS.EN-FR_129896"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="IDIOMS.EN-FR_129896"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="IDIOMS.EN-FR_129896" data-text="Le patron voulait faire un <em>exemple</em> des employés qui arrivaient constamment en retard au travail."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="IDIOMS.EN-FR_129896"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="IDIOMS.EN-FR_129896"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_276153" class="example blocked">
      <div class="src ltr">
        <span class="text">
          He is a classic <em>example</em> of a dedicated team player.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Il est l'<a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> type du joueur d'équipe dévoué.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_276153" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_276153" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_276153"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_276153"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_276153" data-text="Il est l'<em>exemple</em> type du joueur d'équipe dévoué."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_276153"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_276153"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1357907" class="example blocked">
      <div class="src ltr">
        <span class="text">
          This painting is a singular <em>example</em> of his artistic style.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Ce tableau est un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> unique de son style artistique.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1357907" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1357907" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1357907"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1357907"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1357907" data-text="Ce tableau est un <em>exemple</em> unique de son style artistique."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1357907"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1357907"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1288893" class="example blocked">
      <div class="src ltr">
        <span class="text">
          His savory leadership set a positive <em>example</em> for the entire team.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Son leadership exemplaire a donné un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> positif à toute l'équipe.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1288893" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1288893" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1288893"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1288893"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1288893" data-text="Son leadership exemplaire a donné un <em>exemple</em> positif à toute l'équipe."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1288893"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1288893"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1161817" class="example blocked">
      <div class="src ltr">
        <span class="text">
          This book serves as a prime <em>example</em> of excellent literature for young readers.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Ce livre sert d'<a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> idéal de littérature excellente pour les jeunes lecteurs.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1161817" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1161817" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1161817"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1161817"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1161817" data-text="Ce livre sert d'<em>exemple</em> idéal de littérature excellente pour les jeunes lecteurs."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1161817"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1161817"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1161839" class="example blocked">
      <div class="src ltr">
        <span class="text">
          These findings are a prime <em>example</em> of scientific advancements in medicine.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          Ces découvertes sont un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> frappant des avancées scientifiques en médecine.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1161839" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1161839" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1161839"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1161839"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1161839" data-text="Ces découvertes sont un <em>exemple</em> frappant des avancées scientifiques en médecine."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1161839"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1161839"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="CONTEXT-SOURCE2.EN-FR_1073864" class="example blocked">
      <div class="src ltr">
        <span class="text">
          Rust formation is an <em>example</em> of a naturally occurring oxidising process.</span>
      </div>
      <div class="trg ltr">
        <span class="icon jump-right"></span>
        <span class="text" lang='fr'>
          La formation de rouille est un <a class="link_highlighted" href='/translation/french-english/exemple' rel="nofollow"><em>exemple</em></a> de processus d'oxydation naturel.</span>
      </div>
      <div class="options">
        <div class="src hide-mobile-voice">
          <button data-id="CONTEXT-SOURCE2.EN-FR_1073864" class="voice icon stopped" title="Pronunciation" data-lang="en"></button>
          </div>
        <div class="trg">
          <div>
            <button data-id="CONTEXT-SOURCE2.EN-FR_1073864" class="voice icon stopped" title="Pronunciation" data-lang="fr"></button>
            <button class="icon non-favourite show-fav-lists-initiator example-fav " title="Mark this example as favourite" data-example-id="CONTEXT-SOURCE2.EN-FR_1073864"></button>
          </div>
          <div class="grey">
            <button class="copy icon mobile-hidden" title="Copy the translation" data-id="CONTEXT-SOURCE2.EN-FR_1073864"></button>
            <button class="add icon addentry " title="Add this translation to Reverso Collaborative Dictionary" data-url="https://dictionary.reverso.net/CollabDict.aspx?view=2&lang=EN" data-id="CONTEXT-SOURCE2.EN-FR_1073864" data-text="La formation de rouille est un <em>exemple</em> de processus d'oxydation naturel."></button>
            <button class="report icon thumb-down" title="Report a problem in this example" data-id="CONTEXT-SOURCE2.EN-FR_1073864"></button>
            <button class="icon more-context" title="See this translation example in its context (https://context.reverso.net)" data-id="CONTEXT-SOURCE2.EN-FR_1073864"></button>
          </div>
        </div>
      </div>
    </div>
  <div id="blocked-rude-results-banner" class="popup wide-container">
    <div class="header"></div>
    <div class="body">
      <span>Potentially sensitive or inappropriate content</span>
      <div class="buttons-content">
        <button class="button close">Unlock</button>
      </div>
      <span class="disclaimer">
        Examples are used only to help you translate the word or expression searched in various contexts. They are not selected or validated by us and can contain inappropriate terms or ideas. Please report examples to be edited or not to be displayed. Potentially sensitive, inappropriate or colloquial translations are usually marked in red or in orange.</span>
    </div>
  </div>
  <div id="blocked-results-banner" class="popup wide-container">
      <div class="header">
        <button class="icon close"></button>
      </div>
      <div class="body">
        <span>Register to see more examples</span>
        <span>It's <b>simple</b> and it's <b>free</b></span>
        <div class="buttons-content">
          <a href="https://account.reverso.net/Account/Register?lang=en&utm_source=context&utm_medium=banner-see-more-examples&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample" class="button register">Register</a>
          <a href="https://account.reverso.net/Account/Login?lang=en&utm_source=context&utm_medium=banner-see-more-examples&returnUrl=https%3A%2F%2Fcontext.reverso.net%2Ftranslation%2Fenglish-french%2Fexample" class="button login">Connect</a>
        </div>
      </div>
    </div>
  </section>
<section id="no-results">
  <span class="wide-container message">No results found for this meaning.</span>
  <div class="wide-container">
      <button id="add-example3" class="add-example-button" title="Suggest example for &quot;example&quot;" data-ga="display-noexamples">Suggest an example</button>
    </div>
  </section>
<section id="examples-bottom">
  <div class="loading-pannel">
  <span class="icon"></span>
</div>
<div class="buttons-after-examples">
    <button id="load-more-examples" class="button load-more unregistered">Display more examples</button>
    <button id="add-example1" class="add-example-button" title="Suggest example for &quot;example&quot;" data-ga="display-examplesbottom">Suggest an example</button>
    </div>
  </section>
</div>
  <!-- /64378609/Context_MegaBanner_Bottom -->
  <div id="bottom-mega-rca-box">
    <div id='div-gpt-ad-Reverso_Ctxt_D_Result_728x90_BTF' class="bottom-rca mega-rca">
      <script> if(!window.mobilecheck()) { ramjs.que.push(function() { ramjs.business.displayGoogletagSlot('div-gpt-ad-Reverso_Ctxt_D_Result_728x90_BTF') }); } </script>
    </div>
    <span class="rca-title rca-title-vertical bottom-rca">Advertising</span>
  </div>
  <link rel="stylesheet preload" type="text/css" media="all" href="https://cdn.reverso.net/context/v72410/css/dapp_banner.css" as="style"><aside id="dapp-banner-wrapper" class="app-promo-banner dapp-promo nonselectable img-loader new-banner">
  <a id="dapp-banner" data-os="mac" data-dl="https://www.reverso.net/windows-mac-app/en/thanks?utm_source=context&utm_campaign=banner-below-examples" href="https://dl.reverso.net/desktop-app/macos?utm_source=context&utm_campaign=banner-below-examples">
  <div class="dapp-popup-left">
      <div class="dapp-content">
        <div class="dapp-title">
          <p class="title">Learn new vocabulary by reading whatever you like</p>
          <p>After installing Reverso app, double-click words to look them up in articles, documents, emails, PDF…</p>
        </div>
        <div class="dapp-button-wrapper">
          <button class="dapp-dl-button" data-os="mac"><span class="icon download"></span>Download Reverso app</button>
        </div>
      </div>
    </div>
    <div class="dapp-popup-right">
      <img width="280" height="224" alt="" data-src="https://cdn.reverso.net/context/v72410/images/desktopapp/app-img/new-en.png" src="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=">
    </div>
  </a>
</aside>
<div id="home-badges-background">
  <a data-os="android" href="/translation/mobile-app?utm_source=contextweb&utm_medium=storebadge&utm_campaign=ctxthomepage&utm_content=ctxtappdl">
    <div id="home-mobile-badges-apps">
      <div class="text title-text">More features with our <span>free</span> app</div>
      <div class="content">
        <div aria-label="Get it on Google Play - Reverso Context"" class="badge and no-loading en"></div>
        <div aria-label="Download on the App Store - Reverso Context"" class="badge ios no-loading en"></div>
        <div><b>Voice and photo</b> translation, <b>offline</b> features, <b>synonyms</b>, <b>conjugation</b>, <b>learning</b> games</div>
      </div>
    </div>
  </a>
</div><div id="result-info" class="wide-container" data-exact="184873">
      <p>Results: <span id="nrows">184873</span>. Exact: <span id="nrows_exact">184873</span>. Elapsed time: <span id="time">797</span> ms.</p>
    </div>
  </section>
</div>
    <section id="sitemaps">
  <p><a href="/translation/index/english-french/w.html">Word index:</a> <a href="/translation/index/english-french/w-1-300.html">1-300</a>, <a href="/translation/index/english-french/w-301-600.html">301-600</a>, <a href="/translation/index/english-french/w-601-900.html">601-900</a><p><a href="/translation/index/english-french/e.html">Expression index:</a> <a href="/translation/index/english-french/e-1-400.html">1-400</a>, <a href="/translation/index/english-french/e-401-800.html">401-800</a>, <a href="/translation/index/english-french/e-801-1200.html">801-1200</a><p><a href="/translation/index/english-french/p.html">Phrase index:</a> <a href="/translation/index/english-french/p-1-400.html">1-400</a>, <a href="/translation/index/english-french/p-401-800.html">401-800</a>, <a href="/translation/index/english-french/p-801-1200.html">801-1200</a></section>
    <footer>
  <section class="content">
    <div class="promo">
      <div class="area-logo">
        <span class="icon reverso-logo"><span class="path1"></span><span class="path2"></span><span class="path3"></span><span class="path4"></span><span class="path5"></span><span class="path6"></span><span class="path7"></span><span class="path8"></span><span class="path9"></span><span class="path10"></span></span>
      </div>
      <p class="helping-millions">Helping millions of people and large organizations communicate more efficiently and precisely in all languages.</p>
      <div class="social-content">
        <button class="icon font-resize" title="Text size" data-size="0"></button>
        <a class="facebook-link icon facebook" target="_blank" rel="noopener" href="https://www.facebook.com/Reverso.net?ref=ts" title="Facebook"><span class="path1"></span><span class="path2"></a>
        <a class="twitter-link icon twitter" target="_blank" rel="noopener" href="https://twitter.com/ReversoEN" title="Twitter"></a>
        <a class="instagram-link icon instagram" target="_blank" rel="noopener" href="https://www.instagram.com/reverso_app/?hl=en" title="Instagram"></a>
      </div>
    </div>
    <div class="area-products">
      <div class="inner-block">
        <div class="title">Products</div>
        <div class="item">
          <a class="translation" target="_blank" rel="noopener" href="https://www.reverso.net/text-translation">Translate Text</a>
        </div>
        <div class="item">
          <a class="documents" target="_blank" rel="noopener" href="https://documents.reverso.net/Default.aspx?lang=en">Translate Documents</a>
        </div>
        <div class="item">
          <a href="https://context.reverso.net/translation/">Translation in Context</a>
        </div>
        <div class="item">
          <a class="speller_link" target="_blank" rel="noopener" href="https://www.reverso.net/spell-checker/english-spelling-grammar/">Grammar Check</a>
        </div>
        <div class="item">
          <a class="synonyms" target="_blank" rel="noopener" href="https://synonyms.reverso.net/synonym/">Synonyms</a>
        </div>
        <div class="item">
          <a class="conjugator" target="_blank" rel="noopener" href="https://conjugator.reverso.net/conjugation-english.html">Conjugation</a>
        </div>
        <div class="item item-button">
          <span>More<span class="icon down-caret"></span></span>
        </div>
        <div class="item hidden">
          <a class="dictionary" target="_blank" rel="noopener" href="https://dictionary.reverso.net/english-definition/">Dictionary</a>
        </div>
        <div class="item hidden">
          <a class="expressio" target="_blank" rel="noopener" href="https://www.expressio.fr/">Expressio</a>
        </div>
        <div class="item hidden">
          <a class="grammar" target="_blank" rel="noopener" href="https://grammar.reverso.net/">Grammar</a>
        </div>
      </div>
    </div>
    <div class="area-apps">
      <div class="inner-block">
        <div class="title">Free apps</div>
        <div class="item">
          <a class="dapp-link" data-os="mac" href="https://www.reverso.net/windows-mac-app/en?utm_source=context&utm_medium=link-footer"">Reverso for Mac/Windows</a>
        </div>
        <div class="item">
          <a class="app-link" data-os="android" href="https://context.reverso.net/translation/mobile-app/?utm_source=context&utm_medium=link-footer">Reverso for iOS/Android</a>
        </div>
        <div class="item">
          <a class="extension" href="https://www.reverso.net/download-browser-extension?utm_source=context&utm_medium=link-footer">Reverso for Chrome/Edge</a>
        </div>
      </div>
    </div>
    <div class="area-offers">
      <div class="inner-block">
        <div class="title">Offers</div>
        <div class="item">
          <a class="premium" href="https://documents.reverso.net/Pricing.aspx?lang=en&origin=1">Reverso Premium</a>
        </div>
        <div class="item">
          <a class="corporate" href="https://www.corporate-translation.reverso.com/?lang=en">Reverso Corporate Translator</a>
        </div>
      </div>
    </div>
  </section>
  <section class="links legal-links">
    <div class="policies">
      <a target="_blank" rel="noopener" href="https://www.reverso.net/contact/en?origin=3">Contact</a>
      <a href="/translation/about">About Context</a>
      <a href="https://www.reverso.net/disclaimer.aspx?lang=EN">Terms & Conditions</a>
      <a href="javascript:Didomi.preferences.show()">Privacy Settings</a>
      <a href="https://www.reverso.net/privacy.aspx?lang=EN">Privacy Policy</a>
      <a href="/translation/legal">Legal considerations</a>
      <a target="_blank" rel="noopener" href="https://www.welcometothejungle.com/fr/companies/reverso">Careers</a>
    </div>
    <div class="copy">&copy; 2025 Reverso Technologies Inc. All rights reserved.</div>
  </section>
  <section class="links context-links">
    <div>
      <a href="/traduction/">Traduction en contexte</a><a href="/traduccion/">Traducción en contexto</a><a href="/traducao/">Tradução em contexto</a><a href="/traduzione/">Traduzione in contesto</a><a href="/übersetzung/">Übersetzung im Kontext</a><a href="/الترجمة/">الترجمة في السياق</a><a href="/翻訳/">文脈に沿った翻訳</a><a href="/翻译/">情境中的译文</a><a href="/vertaling/">Vertaling in context</a><a href="/תרגום/">תרגום בהקשר</a><a href="/перевод/">Перевод в контексте</a><a href="/tłumaczenie/">Tłumaczenie w kontekście</a><a href="/traducere/">Traducere în context</a><a href="/översättning/">Översättning i sammanhang</a><a href="/çeviri/">İçerik tercümesi</a><a href="/переклад/">Переклад у контексті</a></div>
  </section>
  <section class="links other-links">
    <div class="recommended-links">
      <span class="title">Recommended links: </span>
      <a target="_blank" rel="noopener" class="partner_link" href="https://www.opensubtitles.org">Subtitles for movies and TV series</a></div>
  </section>
  </footer>
<script defer src="https://cdn.reverso.net/context/v72410/js/bst.constants-en.js"></script>
<script defer src="https://cdn.reverso.net/context/v72410/js/main.js"></script>
<script defer src="https://cdn.reverso.net/context/v72410/js/result.js"></script>
<script>
  var ezt = ezt ||[];
  (function(){
    var elem = document.createElement('script');
    elem.src = "https://secure.quantserve.com/aquant.js?a=p-9z6v4xdJwT5Z-";
    elem.async = true;
    elem.type = "text/javascript";
    var scpt = document.getElementsByTagName('script')[0];
    scpt.parentNode.insertBefore(elem,scpt);
  }());
  ezt.push({qacct: 'p-9z6v4xdJwT5Z-', labels: 'context', uid: '' });
</script><noscript><img src="https://pixel.quantserve.com/pixel/p-9z6v4xdJwT5Z-.gif?labels=context" style="display: none;" height="1" width="1" alt=""/>
  <img height="1" width="1" style="display:none" src="https://www.facebook.com/tr?id=1090291004350551&ev=PageView&noscript=1" alt=""/>
</noscript></body>
</html>
""".trimIndent()
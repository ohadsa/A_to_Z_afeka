package com.ohadsa.a_to_z.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.integrativit_client.ui.generic.CostumeDivider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.BuildConfig
import com.ohadsa.a_to_z.MainViewModel
import com.ohadsa.a_to_z.R
import com.ohadsa.a_to_z.login.AvatarCell
import com.ohadsa.a_to_z.login.ConfirmPay
import com.ohadsa.a_to_z.login.LoginActivity
import com.ohadsa.a_to_z.models.MyUser
import com.ohadsa.a_to_z.ui.generic.*
import com.ohadsa.a_to_z.ui.pages.HomePage
import com.ohadsa.a_to_z.ui.pages.PremiumPlan
import com.ohadsa.a_to_z.ui.pages.SubscribeScreen
import com.ohadsa.a_to_z.ui.theme.MyColors
import com.ohadsa.a_to_z.utils.HtmlHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by activityViewModels()
    private var mRewardedAd: RewardedAd? = null

    private val loginLauncher = registerForActivityResult(LoginActivity.Contract()) {
        it?.let {
            println("login id = $it")
        }
    }

    private fun showVideoAd(premium: Boolean) {
        if (!premium) {
            mRewardedAd?.show(requireActivity()) {
                loadVideoAd()
            }
        }
    }

    private fun loadVideoAd() {
        var UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
        if (BuildConfig.DEBUG) {
            UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
        }
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(requireContext(), UNIT_ID,
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    mRewardedAd = rewardedAd
                }
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadVideoAd()
        view.findViewById<ComposeView>(R.id.composeViewGame).setContent {
            var drawerOpen by remember { mutableStateOf(false) }
            val user by viewModel.userData.collectAsState()
            val premium by viewModel.isPremium.collectAsState()
            Box(Modifier.fillMaxSize()) {
                Box {
                    HomePage(
                        viewModel = viewModel,
                        modifier = Modifier.padding(bottom = if(premium) 0.dp else 60.dp) ,
                        onLogoTapped = {
                            HtmlHelper.openDialog(requireActivity(), "about.html")
                        },
                        onSearchTapped = {
                            showVideoAd(premium)
                            findNavController().navigate(R.id.action_to_search)
                        },
                        onMenuTapped = {
                            drawerOpen = true
                        }
                    ) {
                        findNavController().navigate(R.id.action_to_blank)
                        showVideoAd(premium)
                    }
                    SideMenuBar(
                        drawerOpen = drawerOpen,
                        buyCreditTapped = { viewModel.buyCreditTapped(it) },
                        premiumTapped = { viewModel.goPremiumTapped(it) },
                        onClose = { drawerOpen = false },
                        premium = premium,
                        user = user
                    ) {
                        when (it) {
                            MenuAction.ABOUT ->
                                HtmlHelper.openDialog(requireActivity(), "about.html")
                            MenuAction.TERMS ->
                                HtmlHelper.openDialog(requireActivity(), "terms_of_use.html")
                            MenuAction.PRIVACY ->
                                HtmlHelper.openDialog(requireActivity(), "privacy_policy.html")
                            MenuAction.LOGOUT -> {
                                viewModel.logoutTapped()
                                activity?.startActivity(Intent(requireActivity(),
                                    LoginActivity::class.java))
                                activity?.finish()
                            }
                        }
                    }


                }
                if (!premium) {
                    EmbeddedAndroidViewDemo(modifier = Modifier.align(BottomCenter))
                }
            }
        }
    }

}


@Composable
fun SideMenuBar(
    drawerOpen: Boolean,
    onClose: () -> Unit,
    user: MyUser,
    premium:Boolean,
    buyCreditTapped: (BuyCreditPlan) -> Unit,
    premiumTapped: (PremiumPlan) -> Unit,
    openDialog: (MenuAction) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .let { mod ->
                if (drawerOpen)
                    mod
                        .clickableNoFeedback { onClose() }
                        .background(MyColors.darkGray.copy(alpha = 0.8f))
                else mod
            },
        verticalArrangement = Arrangement.Center) {
        AnimatedVisibility(
            visible = drawerOpen,
            enter = slideInHorizontally { -it },
            exit = slideOutHorizontally { -it }
        ) {
            Box(Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.70f)
                .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
                .background(Color.White,
                    RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))) {

                SlideMenu(user, buyCreditTapped, premiumTapped, premium) {
                    openDialog(it)
                }
            }
        }
    }
}

@Composable
fun SlideMenu(
    user: MyUser,
    buyCreditTapped: (BuyCreditPlan) -> Unit,
    premiumTapped: (PremiumPlan) -> Unit,
    premium:Boolean,
    action: (MenuAction) -> Unit,
) {
    var openBuyPopup by remember {
        mutableStateOf(false)
    }
    var openPremiumPopup by remember {
        mutableStateOf(false)
    }
    Column(Modifier
        .fillMaxSize()
        .background(MyColors.main5), verticalArrangement = Arrangement.SpaceBetween) {
        Column {
            Column(
                modifier = Modifier
                    .background(MyColors.main25),
                horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(16.dp))
                AvatarCell(user.avatar, false)
                MyText(text = user.username, font = MyFont.Heading3, color = MyColors.main)
                Row(Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp)
                    )
                    .padding(start = 12.dp, end = 12.dp, top = 18.dp, bottom = 8.dp)
                    .clickableNoFeedback { openBuyPopup = true },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = CenterVertically) {
                    Column {
                        MyText(
                            text = stringResource(id = R.string.favoritesCredit) + " " + user.favoriteCredit,
                            font = MyFont(
                                weight = FontWeight.ExtraBold,
                                textSize = 16.sp,
                                fontName = FontName.DMSans,
                            ),
                            color = MyColors.main,
                        )
                        MyText(
                            text = stringResource(id = R.string.wishListCredit) + "  " + user.wishCredit,
                            font = MyFont(
                                weight = FontWeight.ExtraBold,
                                textSize = 16.sp,
                                fontName = FontName.DMSans,
                            ),
                            color = MyColors.main,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        MyText(
                            text = "Click the box to buy more...",
                            font = MyFont(
                                weight = FontWeight.W400,
                                textSize = 12.sp,
                                fontName = FontName.DMSans,
                                fontStyle = FontStyle.Italic
                            ),
                            color = MyColors.main65,
                        )
                    }
                    DrawableImage(id = R.drawable.gold_star,
                        svg = false,
                        modifier = Modifier
                            .padding(end = 12.dp, bottom = 18.dp)
                            .size(42.dp))
                }
                if(!premium) {
                    Row(Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
                        .background(Color.White, RoundedCornerShape(8.dp)
                        )
                        .padding(start = 12.dp, end = 12.dp, top = 18.dp, bottom = 8.dp)
                        .clickableNoFeedback { openPremiumPopup = true },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically) {
                        Column {
                            MyText(
                                text = stringResource(id = R.string.go_premium),
                                font = MyFont(
                                    weight = FontWeight.ExtraBold,
                                    textSize = 16.sp,
                                    fontName = FontName.DMSans,
                                ),
                                color = MyColors.main,
                            )
                            MyText(
                                text = "Click the box to subscribe...",
                                font = MyFont(
                                    weight = FontWeight.W400,
                                    textSize = 12.sp,
                                    fontName = FontName.DMSans,
                                    fontStyle = FontStyle.Italic
                                ),
                                color = MyColors.main65,
                            )
                        }
                    }
                }

                CostumeDivider(color = MyColors.darkGray,
                    modifier = Modifier.padding(top = 12.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                MyText(
                    modifier = Modifier.clickableNoFeedback { action(MenuAction.TERMS) },
                    text = stringResource(id = R.string.use_terms),
                    font = MyFont.Heading5,
                    color = MyColors.darkGray)
                Spacer(modifier = Modifier.height(16.dp))
                MyText(
                    modifier = Modifier.clickableNoFeedback { action(MenuAction.PRIVACY) },
                    text = stringResource(id = R.string.privacy),
                    font = MyFont.Heading5,
                    color = MyColors.darkGray)
                Spacer(modifier = Modifier.height(16.dp))
                MyText(
                    modifier = Modifier.clickableNoFeedback { action(MenuAction.ABOUT) },
                    text = stringResource(id = R.string.about),
                    font = MyFont.Heading5,
                    color = MyColors.darkGray)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Column {
            MyText(
                modifier = Modifier
                    .padding(start = 18.dp)
                    .clickableNoFeedback { action(MenuAction.LOGOUT) },
                text = stringResource(id = R.string.log_out),
                font = MyFont.Heading5,
                color = MyColors.darkGray)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
    if (openBuyPopup) {
        AnimatedDialog {
            BuyCreditScreen(
                onBack = { openBuyPopup = false },
            ) {
                buyCreditTapped(it)
                openBuyPopup = false
            }
        }
    }

    if (openPremiumPopup) {
        AnimatedDialog {
            SubscribeScreen(
                onBack = { openPremiumPopup = false },
            ) {
                premiumTapped(it)
                openBuyPopup = false
            }
        }
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BuyCreditScreen(
    onBack: () -> Unit,
    buyTapped: (BuyCreditPlan) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    var plan by remember {
        mutableStateOf(BuyCreditPlan.HundredCredits)
    }


    fun showSheet(show: Boolean) =
        scope.launch {
            if (show) sheetState.show()
            else sheetState.hide()
        }
    ModalBottomSheetLayout(
        sheetShape = MaterialTheme.shapes.large.copy(
            topStart = CornerSize(12.dp), topEnd = CornerSize(12.dp)
        ),
        sheetState = sheetState,
        sheetContent = {
            ConfirmPay(
                price = when (plan) {
                    BuyCreditPlan.HundredCredits -> 49.99
                    BuyCreditPlan.TwentyCredits -> 14.99
                    BuyCreditPlan.FiveCredits -> 4.99
                },
                onChoose = {
                    showSheet(false)
                    buyTapped(plan)
                },
                onBack = {
                    showSheet(false)
                }
            )
        },
        sheetBackgroundColor = Color.White
    ) {
        Column(Modifier
            .fillMaxSize()
            .clickableNoFeedback { }
            .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            ClickableTopBar(left = null,
                leftId = R.drawable.chevron_left,
                middle = stringResource(
                    id = R.string.movie_world),
                right = null,
                onLeft = {
                    onBack()
                })
            CostumeDivider(color = MyColors.gray50,
                modifier = Modifier.padding(top = 16.dp)
            )
            Column(Modifier
                .fillMaxSize()
                .background(MyColors.main25)
                .padding(horizontal = 24.dp)
                .padding(top = 12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                MyText(
                    modifier = Modifier,
                    text = stringResource(id = R.string.buyCredits),
                    font = MyFont.GiantHeading,
                    color = MyColors.indigoPrimary,
                    lineHeight = MyFont.GiantHeading.lineHeight)
                MyText(
                    modifier = Modifier,
                    text = stringResource(id = R.string.credits_description),
                    font = MyFont.Heading5,
                    color = MyColors.gray65,
                    lineHeight = MyFont.Heading5.lineHeight)
                CreditPlansCell {
                    plan = it
                    showSheet(true)
                }
            }
        }
    }
}

@Composable
fun CreditPlansCell(onChoose: (BuyCreditPlan) -> Unit) {
    Column {
        Row(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clickableNoFeedback { onChoose(BuyCreditPlan.HundredCredits) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                MyText(
                    text = stringResource(id = R.string.hundredCredits) + "   -   " + "49.99$",
                    font = MyFont(
                        weight = FontWeight.ExtraBold,
                        textSize = 16.sp,
                        fontName = FontName.DMSans,
                    ),
                    color = MyColors.main,
                )
            }
            DrawableImage(id = R.drawable.gold_star,
                svg = false,
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 18.dp)
                    .size(42.dp))
        }



        Row(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clickableNoFeedback { onChoose(BuyCreditPlan.TwentyCredits) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                MyText(
                    text = stringResource(id = R.string.twentyCredits) + "  -  " + "14.99$",
                    font = MyFont(
                        weight = FontWeight.ExtraBold,
                        textSize = 16.sp,
                        fontName = FontName.DMSans,
                    ),
                    color = MyColors.main,
                )
            }
            DrawableImage(id = R.drawable.star,
                svg = false,
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 18.dp)
                    .size(42.dp))
        }



        Row(Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .border(2.dp, MyColors.main, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp)
            )
            .height(120.dp)
            .padding(horizontal = 12.dp)
            .clickableNoFeedback { onChoose(BuyCreditPlan.FiveCredits) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column {
                MyText(
                    text = stringResource(id = R.string.fiveCredits) + "  -  " + "4.99$",
                    font = MyFont(
                        weight = FontWeight.ExtraBold,
                        textSize = 16.sp,
                        fontName = FontName.DMSans,
                    ),
                    color = MyColors.main,
                )
            }
        }
    }
}

enum class MenuAction {
    ABOUT, TERMS, PRIVACY, LOGOUT
}

enum class BuyCreditPlan {
    FiveCredits, TwentyCredits, HundredCredits
}

@Composable
fun EmbeddedAndroidViewDemo(modifier: Modifier) {
    Column(modifier
        .fillMaxWidth()
        .height(60.dp)) {

        AndroidView(
            modifier = Modifier
                .fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    loadAd(AdRequest.Builder().build())
                }
            }
        )

    }
}

fun getAdSize(width: Float, height: Float, ctx: Context): AdSize {
    val adWidth = (width / height).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, adWidth)
}
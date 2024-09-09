package com.jetbrains.notes.ui.shapes

import android.annotation.SuppressLint
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import com.example.compose.AppTheme
import com.jetbrains.notes.R

@Composable
fun CustomShapes() {
    Box(modifier = Modifier
        .fillMaxSize()
        .drawWithCache {
            val roundedPolygon = RoundedPolygon(
                numVertices = 3,
                radius = 200.dp.toPx(),
                centerX = size.width / 2,
                centerY = size.height / 2,
                rounding = CornerRounding(radius = 100f, smoothing = 1f)
            )

            val path = roundedPolygon
                .toPath()
                .asComposePath()
            onDrawBehind {
                drawPath(
                    path = path,
                    color = Color.Blue
                )
//                drawCircle(
//                    color = Color.Red,
//                    radius = 100f
//                )
            }
        }) {

    }
}

@Preview(showBackground = true)
@Composable
fun CustomShapesPreview() {
    AppTheme {
        CustomShapes()
    }
}


@Composable
fun MorphShapes() {
    val infiniteAnimation = rememberInfiniteTransition(label = "infinite animation")
    val morphProgress = infiniteAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "morph"
    )
    Box(
        modifier = Modifier
            .drawWithCache {
                val triangle = RoundedPolygon(
                    numVertices = 6,
                    radius = size.minDimension / 2f,
                    centerX = size.width / 2f,
                    centerY = size.height / 2f,
                    rounding = CornerRounding(
                        size.minDimension / 10f,
                        smoothing = 0.1f
                    )
                )
                val square = RoundedPolygon(
                    numVertices = 5,
                    radius = size.minDimension / 2f,
                    centerX = size.width / 2f,
                    centerY = size.height / 2f
                )

                val morph = Morph(start = triangle, end = square)
                val morphPath = morph
                    .toPath(progress = morphProgress.value)
                    .asComposePath()

                onDrawBehind {
                    drawPath(morphPath, color = Color.Black)
                }
            }
            .fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun Morph() {
    AppTheme {
        MorphShapes()
    }
}

@Composable
fun ClipShapes() {
    val hexagon = remember {
        RoundedPolygon(
            6,
            rounding = CornerRounding(0.2f)
        )
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dog1),
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .graphicsLayer {
                    this.shadowElevation = 16.dp.toPx()
                    this.clip = true
                    this.shape = RoundedCornerShape(topStart = 50.dp, bottomEnd = 50.dp)
                    this.ambientShadowColor = Color.Black
                    this.spotShadowColor = Color.Black
                }
                .size(200.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Clip() {
    AppTheme {
        ClipShapes()
    }
}

private class MorphPolygonShape(private val morph: Morph, private val percentage: Float) : Shape {
    private val matrix = androidx.compose.ui.graphics.Matrix()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)

        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)
        return Outline.Generic(path)
    }


}

@SuppressLint("RememberReturnType")
@Composable
fun MorphOnClick() {
    val shapeA = remember {
        RoundedPolygon(
            3,
            rounding = CornerRounding(0.2f)
        )
    }
    val shapeB = remember {
        RoundedPolygon.star(
            6,
            rounding = CornerRounding(0.1f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )

    Box(
        modifier = Modifier
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.dog1),
            contentDescription = "Dog",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(MorphPolygonShape(morph, animatedProgress.value))
//                .graphicsLayer {
//                    this.shadowElevation = 16.dp.toPx()
//                    this.clip = true
//                    this.shape = MorphPolygonShape(morph, animatedProgress.value)
//                    this.ambientShadowColor = Color.Black
//                    this.spotShadowColor = Color.Black
//                }
                .size(200.dp)
                .padding(8.dp)
                .clickable(interactionSource = interactionSource, indication = null) {
                }
        )

    }
}

@Preview(showBackground = true)
@Composable
fun MorphhClick() {
    AppTheme {
        MorphOnClick()
    }
}


@Composable
fun OverlappingImages() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
            .background(Color.LightGray)
    ) {

        Image(
            painter = painterResource(id = R.drawable.dog1),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .border(
                    BorderStroke(4.dp, Color.White),
                    shape = RoundedCornerShape(50.dp)
                )
                .clip(RoundedCornerShape(50.dp))
                .align(Alignment.TopStart)
        )


        Image(
            painter = painterResource(id = R.drawable.dog2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
//                .border(
//                    BorderStroke(4.dp, Color.White),
//                    shape = RoundedCornerShape(50.dp)
//                )
                .clip(RoundedCornerShape(50.dp))
                .offset((20).dp, (-20).dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun overlap() {
    AppTheme {
        OverlappingImages()
    }
}

@Composable
fun OffScreenImage() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dog3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .aspectRatio(1f)
                .size(200.dp)
                .background(brush = Brush.linearGradient(listOf(Color.Blue, Color.White)))
                .padding(20.dp)
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
//                .clip(RoundedCornerShape(50))
                .drawWithCache {
                    val path = Path()
                    path.addOval(
                        Rect(
                            topLeft = Offset.Zero,
                            bottomRight = Offset(size.width, size.height)
                        )
                    )
                    onDrawWithContent {
                        clipPath(path) {
                            this@onDrawWithContent.drawContent()
                        }
                        val dotSize = size.width / 8f
                        drawCircle(
                            Color.White,
                            radius = dotSize,
                            center = Offset(
                                x = size.width - dotSize,
                                y = size.height - dotSize
                            ),
                            blendMode = BlendMode.Clear
                        )
                        drawCircle(
                            Color(0xFF7DFF82), radius = dotSize * 0.8f,
                            center = Offset(
                                x = size.width - dotSize,
                                y = size.height - dotSize
                            )
                        )

                    }

                }
        )
    }
}

@Preview
@Composable
fun OffScreen() {
    AppTheme {
        OffScreenImage()
    }
}


@Composable
fun BottomBarWithFloatingButton() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .align(Alignment.BottomCenter)
            .background(Color.Green)
            .padding(6.dp)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
            }
            .drawWithCache {
                val path = Path()
                path.addOval(
                    Rect(
                        topLeft = Offset.Zero,
                        bottomRight = Offset(size.width, size.height)
                    )
                )
                onDrawWithContent {
                    clipPath(path) {
                        this@onDrawWithContent.drawContent()
                    }
                    val dotSize = size.width / 8f
                    drawCircle(
                        Color.White,
                        radius = dotSize,
                        center = Offset(
                            x = size.width / 2f,
                            y = size.height - dotSize
                        ),
                        blendMode = BlendMode.Clear
                    )
                    drawCircle(
                        Color.Red, radius = dotSize * 0.8f,
                        center = Offset(
                            x = size.width / 2f,
                            y = size.height - dotSize
                        )
                    )

                }

            }) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBar() {
    AppTheme {
        BottomBarWithFloatingButton()
    }
}

@Composable
fun BottomBarWithCenterFab() {

    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(65.dp)
                    .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .displayCutoutPadding()
                    .shadow(elevation = 22.dp),
                //backgroundColor = Color.White,
//                elevation = 22.dp
            ) {
                IconButton(onClick = { /* Handle home click */ }) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
                Spacer(modifier = Modifier.weight(1f, true))
                IconButton(onClick = { /* Handle profile click */ }) {
                    Icon(Icons.Default.Person, contentDescription = "Profile")
                }
            }
        },
        floatingActionButton = {
            Box(
                Modifier
                    .height(72.dp) // Height to ensure FAB floats above
//                    .padding(top = 28.dp) // Lift the FAB
            ) {
                FloatingActionButton(
                    onClick = { /* Handle FAB click */ },
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "FAB")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        // Your main content here
        Box(modifier = Modifier.padding(innerPadding)) {
            // Add your main content here
        }
    }
}


@Preview
@Composable
fun newMenuPreview() {
    AppTheme {
        BottomBarWithCenterFab()
    }
}

@Composable
fun CustomBottomBarWithFloatingButton() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu 1", tint = Color.White)
                    }
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Menu 2", tint = Color.White)
                    }
                }

                Row {
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Menu 3", tint = Color.White)
                    }
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu 4", tint = Color.White)
                    }
                }
            }

        }


        Box(
            modifier = Modifier
                .size(56.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-28).dp)
                .border(8.dp, Color.White, RoundedCornerShape(50))

        ) {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                contentColor = Color.White,
                shape = RoundedCornerShape(50),
//                elevation = FloatingActionButtonDefaults.elevation(26.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }
}

@Preview
@Composable
fun newMenuPreview2() {
    AppTheme {
        CustomBottomBarWithFloatingButton()
    }
}

@Composable
fun AnimatedBotomNav(){

}




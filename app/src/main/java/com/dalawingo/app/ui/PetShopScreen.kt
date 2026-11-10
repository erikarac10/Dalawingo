package com.dalawingo.app.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.dalawingo.app.data.LearnerProgress
import com.dalawingo.app.model.Cosmetic
import com.dalawingo.app.model.CosmeticSlot
import com.dalawingo.app.model.cosmetics
import com.dalawingo.app.model.pets

private fun LearnerProgress.equippedFor(slot: CosmeticSlot): String? = when (slot) {
    CosmeticSlot.HAT -> equippedHat
    CosmeticSlot.OUTFIT -> equippedOutfit
    CosmeticSlot.ACCESSORY -> equippedAccessory
    CosmeticSlot.ROOM -> equippedRoom
}

private fun emojiFor(id: String?): String? = cosmetics.firstOrNull { it.id == id }?.emoji

/** A purchased room recolors the entire full-bleed Player header, status bar included. */
fun petRoomColor(room: String): Color = when (room) {
    "room_sky" -> Color(0xFFD8EFFD)
    "room_stars" -> Color(0xFFDDD1F7)
    "room_forest" -> Color(0xFFDBEFCF)
    else -> Color(0xFFF9D6ED)
}

@Composable
fun PetPreview(
    progress: LearnerProgress,
    modifier: Modifier = Modifier,
    preview: Cosmetic? = null,
    onEdit: (() -> Unit)? = null,
    embedHeader: Boolean = false,
    onPesos: (() -> Unit)? = null
) {
    val room = if (preview?.slot == CosmeticSlot.ROOM) preview.id else progress.equippedRoom
    val background = petRoomColor(room)
    val hat = emojiFor(if (preview?.slot == CosmeticSlot.HAT) preview.id else progress.equippedHat)
    val outfit = emojiFor(if (preview?.slot == CosmeticSlot.OUTFIT) preview.id else progress.equippedOutfit)
    val accessory = emojiFor(if (preview?.slot == CosmeticSlot.ACCESSORY) preview.id else progress.equippedAccessory)
    val animal = pets.firstOrNull { it.id == progress.animalId } ?: pets.first()
    // Quiet idle movement makes the companion feel alive without distracting from quests.
    val idleTransition = rememberInfiniteTransition(label = "Pet breathing")
    val idle by idleTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400), RepeatMode.Reverse),
        label = "Pet idle"
    )

    Box(
        modifier.then(if (embedHeader) Modifier else Modifier.clip(RoundedCornerShape(22.dp)))
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        if (embedHeader && onPesos != null) {
            StatsHeader(
                progress = progress,
                onPesos = onPesos,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 7.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = if (embedHeader) Modifier.offset(y = (18f - 3f * idle).dp)
                else Modifier.offset(y = (-2f * idle).dp)
        ) {
            Box(Modifier.size(width = 156.dp, height = 112.dp), contentAlignment = Alignment.Center) {
                if (animal.id == "tarsier") {
                    TarsierMascot(Modifier.size(105.dp))
                } else {
                    Text(animal.emoji, fontSize = 81.sp)
                }
                if (hat != null) Text(hat, Modifier.align(Alignment.TopCenter).offset(y = (-8).dp), fontSize = 31.sp)
                if (accessory != null) Text(accessory, Modifier.align(Alignment.CenterEnd).offset(x = (-8).dp, y = 15.dp), fontSize = 24.sp)
            }
            if (outfit != null) Text(outfit, fontSize = 26.sp)
        }
        if (onEdit != null) {
            RaisedIconButton(
                glyph = GameGlyph.EDIT,
                description = "Customize pet",
                onClick = onEdit,
                modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp),
                size = 46.dp,
                face = Color.White,
                shade = Color(0xFFC5A7B6),
                iconColor = Color(0xFF735369)
            )
        }
    }
}

@Composable
fun PesosWalletDialog(
    pesos: Int,
    onClose: () -> Unit,
    onShop: () -> Unit
) {
    Dialog(onDismissRequest = onClose) {
        Column(
            Modifier.fillMaxWidth().heightIn(max = 610.dp)
                .clip(RoundedCornerShape(24.dp)).background(Color.White)
                .verticalScroll(rememberScrollState()).padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                RaisedIconButton(GameGlyph.CLOSE, "Close wallet", onClose, size = 38.dp)
            }
            Text("🪙", fontSize = 52.sp)
            Heading("Your Pesos", 25.sp)
            Spacer(Modifier.height(5.dp))
            Text("$pesos", color = Tangerine, fontWeight = FontWeight.ExtraBold, fontSize = 31.sp)
            Spacer(Modifier.height(5.dp))
            Spacer(Modifier.height(14.dp))
            Text("HOW TO EARN", Modifier.fillMaxWidth(), color = Ink, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(11.dp))
            listOf(
                Triple("📖", "Complete 4 lesson rounds", "+20"),
                Triple("👑", "First perfect score", "+10"),
                Triple("🏆", "Complete a unit", "+50"),
                Triple("📅", "First daily practice", "+25"),
                Triple("🥉", "Bronze daily quest", "+5"),
                Triple("🥈", "Silver daily quest", "+10"),
                Triple("🥇", "Gold daily quest", "+15"),
                Triple("🎖️", "Claim achievements", "VARIES")
            ).forEach { (symbol, label, amount) ->
                Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(symbol, fontSize = 20.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(label, Modifier.weight(1f), fontSize = 13.sp, color = Ink)
                    Text(amount, color = Tangerine, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Lesson bonuses pay once. Daily quests reset at midnight.",
                fontSize = 12.sp, color = Muted, textAlign = TextAlign.Center)
            Spacer(Modifier.height(17.dp))
            DalaButton("🛍️  VISIT SHOP  →", onShop, Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun CosmeticShopScreen(
    progress: LearnerProgress,
    onClose: () -> Unit,
    onBuy: (String) -> Boolean,
    onEquip: (String) -> Unit,
    onWardrobe: () -> Unit
) {
    var category by remember { mutableStateOf("All") }
    var selected by remember { mutableStateOf<Cosmetic?>(null) }
    val categories = listOf("All", "Hats", "Clothes", "Accessories", "Rooms")
    val shown = cosmetics.filter {
        when (category) {
            "Hats" -> it.slot == CosmeticSlot.HAT
            "Clothes" -> it.slot == CosmeticSlot.OUTFIT
            "Accessories" -> it.slot == CosmeticSlot.ACCESSORY
            "Rooms" -> it.slot == CosmeticSlot.ROOM
            else -> true
        }
    }

    Column(Modifier.fillMaxSize().background(Color.White).padding(horizontal = 17.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            RaisedIconButton(GameGlyph.CLOSE, "Close", onClose, size = 40.dp)
            Spacer(Modifier.width(8.dp))
            Heading("Shop", 25.sp, Modifier.weight(1f))
            Text("🪙 ${progress.pesos}", color = Tangerine, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
        }
        PetPreview(progress, Modifier.fillMaxWidth().height(154.dp), preview = selected)
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.weight(1f))
            Text("WARDROBE →", Modifier.clickable(onClick = onWardrobe).padding(6.dp),
                color = LeafDark, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(9.dp))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            categories.forEach { name ->
                Box(
                    Modifier.clip(RoundedCornerShape(18.dp))
                        .background(if (category == name) Leaf else Pale)
                        .clickable { category = name }
                        .padding(horizontal = 13.dp, vertical = 10.dp)
                ) {
                    Text(name, color = if (category == name) Color.White else Ink,
                        fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(shown, key = { it.id }) { item ->
                val owned = item.id in progress.ownedCosmetics
                val canUnlock = progress.xp >= item.xpNeeded && progress.golden.size >= item.goldenNeeded
                val itemTint = when (item.slot) {
                    CosmeticSlot.HAT -> Color(0xFFFFE9F2)
                    CosmeticSlot.OUTFIT -> Color(0xFFE4F3FF)
                    CosmeticSlot.ACCESSORY -> Color(0xFFFFF1D8)
                    CosmeticSlot.ROOM -> Color(0xFFE8F7DF)
                }
                val highlighted = selected?.id == item.id
                Column(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
                        .background(if (highlighted) itemTint.copy(alpha = .55f) else Color.White)
                        .border(2.dp, if (highlighted) Leaf else Border,
                            RoundedCornerShape(18.dp))
                        .clickable { selected = item }.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.size(79.dp).clip(RoundedCornerShape(21.dp))
                        .background(itemTint), contentAlignment = Alignment.Center) {
                        Text(item.emoji, fontSize = 48.sp)
                    }
                    Spacer(Modifier.height(7.dp))
                    Text(item.name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        when {
                            owned -> "✓ OWNED"
                            item.achievementOnly -> "🏆 ACHIEVEMENT"
                            !canUnlock -> "🔒 LOCKED"
                            item.price == 0 -> "🎁 CLAIM"
                            else -> "🪙 ${item.price}"
                        },
                        color = if (owned) LeafDark else if (canUnlock) Tangerine else Muted,
                        fontWeight = FontWeight.ExtraBold, fontSize = 12.sp
                    )
                }
            }
        }
    }

    selected?.let { item ->
        val owned = item.id in progress.ownedCosmetics
        val equipped = progress.equippedFor(item.slot) == item.id
        val requirementsMet = progress.xp >= item.xpNeeded && progress.golden.size >= item.goldenNeeded
        val affordable = progress.pesos >= item.price
        val available = !equipped && (owned || (!item.achievementOnly && requirementsMet && affordable))
        AlertDialog(
            onDismissRequest = { selected = null },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White,
            title = { Text("${item.emoji}  ${item.name}", fontWeight = FontWeight.ExtraBold) },
            text = {
                Column {
                    Text(when {
                        item.achievementOnly -> "Earn this exclusive item from the Achievements collection in Progress."
                        item.goldenNeeded > 0 -> "Earn ${item.goldenNeeded} golden lesson(s) to unlock this reward."
                        item.xpNeeded > 0 -> "Requires ${item.xpNeeded} total XP."
                        item.price > 0 -> "Price: ${item.price} Pesos"
                        else -> "Free starter item"
                    }, color = Ink)
                    if (!owned && !affordable && requirementsMet) {
                        Spacer(Modifier.height(7.dp))
                        Text("You need ${item.price - progress.pesos} more Pesos.", color = Muted)
                    }
                }
            },
            confirmButton = {
                DalaButton(
                    when {
                        equipped -> "EQUIPPED"
                        owned -> "EQUIP"
                        item.achievementOnly -> "ACHIEVEMENT REWARD"
                        !requirementsMet -> "LOCKED"
                        !affordable -> "NOT ENOUGH PESOS"
                        item.price == 0 -> "CLAIM & EQUIP"
                        else -> "BUY & EQUIP"
                    },
                    onClick = {
                        if (owned || onBuy(item.id)) {
                            onEquip(item.id)
                            selected = null
                        }
                    },
                    Modifier.width(195.dp), enabled = available
                )
            },
            dismissButton = { DalaOutlineButton("CLOSE", { selected = null }, Modifier.width(90.dp)) }
        )
    }
}

@Composable
fun PetWardrobeScreen(
    progress: LearnerProgress,
    onClose: () -> Unit,
    onChoosePet: (String) -> Unit,
    onEquip: (String) -> Unit,
    onUnequip: (CosmeticSlot) -> Unit,
    onShop: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color.White).verticalScroll(rememberScrollState())
        .padding(horizontal = 18.dp)) {
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            RaisedIconButton(GameGlyph.CLOSE, "Close", onClose, size = 40.dp)
            Spacer(Modifier.width(8.dp))
            Heading("Customize", 25.sp)
        }
        PetPreview(progress, Modifier.fillMaxWidth().height(170.dp))
        Spacer(Modifier.height(14.dp))
        Heading("Choose your pet", 19.sp)
        Spacer(Modifier.height(10.dp))
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            pets.forEach { pet ->
                val locked = pet.id == "deer" && pet.id !in progress.unlockedAnimals
                Column(
                    Modifier.clip(RoundedCornerShape(14.dp))
                        .background(if (progress.animalId == pet.id) Leaf.copy(alpha = .18f) else Pale)
                        .clickable(enabled = !locked) { onChoosePet(pet.id) }.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(if (locked) "🔒" else pet.emoji, fontSize = 32.sp)
                    Text(pet.name, color = if (locked) Muted else Ink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    if (locked) Text("ACHIEVEMENT", color = Muted, fontSize = 9.sp)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        Heading("Your wardrobe", 19.sp)
        Spacer(Modifier.height(9.dp))
        CosmeticSlot.entries.forEach { slot ->
            Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(slot.name.lowercase().replaceFirstChar { it.uppercase() },
                    Modifier.weight(1f), color = Ink, fontWeight = FontWeight.ExtraBold)
                if (slot != CosmeticSlot.ROOM && progress.equippedFor(slot) != null) {
                    Text("REMOVE", Modifier.clickable { onUnequip(slot) }.padding(6.dp),
                        color = Sky, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            val owned = cosmetics.filter { it.slot == slot && it.id in progress.ownedCosmetics }
            if (owned.isEmpty()) Text("No items yet — visit the Shop!", color = Muted, fontSize = 13.sp)
            owned.forEach { item ->
                val equipped = progress.equippedFor(slot) == item.id
                Row(
                    Modifier.fillMaxWidth().padding(bottom = 7.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(if (equipped) Leaf.copy(alpha = .1f) else Pale)
                        .clickable { onEquip(item.id) }.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.emoji, fontSize = 24.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(item.name, Modifier.weight(1f), color = Ink, fontWeight = FontWeight.Bold)
                    Text(if (equipped) "EQUIPPED" else "EQUIP", fontSize = 11.sp,
                        color = LeafDark, fontWeight = FontWeight.ExtraBold)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(9.dp))
        DalaButton("🛍️  VISIT SHOP", onShop, Modifier.fillMaxWidth())
        Spacer(Modifier.height(24.dp))
    }
}

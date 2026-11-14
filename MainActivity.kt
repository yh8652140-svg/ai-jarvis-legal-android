package ma.jarvislegal.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val navy = androidx.compose.ui.graphics.Color(0xFF101B33)
private val gold = androidx.compose.ui.graphics.Color(0xFFC7A45B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { JarvisLegalApp() }
    }
}

@Composable
fun JarvisLegalApp() {
    var tab by remember { mutableStateOf("Accueil") }
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = navy, secondary = gold, background = androidx.compose.ui.graphics.Color(0xFFF5F6FA)
        )
    ) {
        Scaffold(
            topBar = {
                Surface(color = navy) {
                    Column(Modifier.fillMaxWidth().padding(18.dp)) {
                        Text("AI JARVIS LEGAL", color = gold, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Préparation au concours de la magistrature — Maroc", color = androidx.compose.ui.graphics.Color.White, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            bottomBar = {
                NavigationBar {
                    listOf("Accueil", "QCM", "Cas pratiques", "Jurisprudence", "Planning").forEach { item ->
                        NavigationBarItem(
                            selected = tab == item,
                            onClick = { tab = item },
                            icon = { Text(when(item) { "Accueil" -> "⌂"; "QCM" -> "?"; "Cas pratiques" -> "§"; "Jurisprudence" -> "⚖"; else -> "▦" }) },
                            label = { Text(item, maxLines = 1) }
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (tab) {
                    "Accueil" -> HomeScreen(onNavigate = { tab = it })
                    "QCM" -> QuizScreen()
                    "Cas pratiques" -> CasesScreen()
                    "Jurisprudence" -> JurisprudenceScreen()
                    else -> PlanningScreen()
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String, subtitle: String) {
    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = navy)
    Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    SectionTitle("Tableau de bord", "Ton espace de préparation juridique")
    Panel {
        Text("Bienvenue dans ton assistant de préparation.", fontWeight = FontWeight.SemiBold)
        Text("Organise tes révisions, entraîne-toi et consolide tes connaissances.")
    }
    listOf(
        Triple("QCM", "Teste tes connaissances et consulte les corrections.", "Commencer"),
        Triple("Cas pratiques", "Applique le raisonnement juridique à des situations concrètes.", "S'entraîner"),
        Triple("Jurisprudence", "Retrouve les fiches de jurisprudence classées par matière.", "Explorer"),
        Triple("Planning", "Suis tes objectifs de révision.", "Voir le planning")
    ).forEach { (title, desc, action) ->
        Panel {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(desc)
            Button(onClick = { onNavigate(title) }) { Text(action) }
        }
    }
    Text("Contenu pédagogique de démonstration : vérifie les textes et références dans les sources officielles.", style = MaterialTheme.typography.bodySmall)
}

private data class Question(val title: String, val answers: List<String>, val correct: Int, val explanation: String)

@Composable
fun QuizScreen() {
    val questions = remember {
        listOf(
            Question(
                "Quelle juridiction exerce en principe le contrôle de constitutionnalité des lois au Maroc ?",
                listOf("La Cour constitutionnelle", "La Cour de cassation", "La Cour des comptes", "Le tribunal administratif"),
                0, "La Cour constitutionnelle exerce les compétences prévues par la Constitution, notamment en matière de contrôle de constitutionnalité."
            ),
            Question(
                "Quel principe impose au juge de statuer dans les limites de ce qui lui est demandé ?",
                listOf("Le contradictoire", "Le principe dispositif", "La publicité", "La collégialité"),
                1, "Le principe dispositif encadre notamment l'objet du litige et les prétentions des parties."
            ),
            Question(
                "En matière pénale, la présomption d'innocence bénéficie :",
                listOf("À toute personne poursuivie", "Uniquement aux témoins", "Uniquement après jugement", "Aux seules personnes condamnées"),
                0, "La présomption d'innocence est une garantie fondamentale de la procédure pénale."
            )
        )
    }
    var index by remember { mutableIntStateOf(0) }
    var selected by remember { mutableIntStateOf(-1) }
    var score by remember { mutableIntStateOf(0) }
    var submitted by remember { mutableStateOf(false) }
    SectionTitle("Entraînement QCM", "Questions de démonstration — niveau concours")
    Panel {
        Text("Question ${index + 1} / ${questions.size}", color = gold, fontWeight = FontWeight.Bold)
        Text(questions[index].title, fontWeight = FontWeight.SemiBold)
        questions[index].answers.forEachIndexed { i, answer ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(selected == i, onClick = { if (!submitted) selected = i })
                Text(answer)
            }
        }
        if (submitted) {
            Text(if (selected == questions[index].correct) "Bonne réponse !" else "Réponse incorrecte.", color = if (selected == questions[index].correct) androidx.compose.ui.graphics.Color(0xFF18794E) else MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            Text(questions[index].explanation)
        }
        Button(
            enabled = selected >= 0 && !submitted,
            onClick = { submitted = true; if (selected == questions[index].correct) score++ }
        ) { Text("Valider la réponse") }
        if (submitted) {
            Button(onClick = {
                if (index < questions.lastIndex) { index++; selected = -1; submitted = false }
                else { index = 0; selected = -1; submitted = false; score = 0 }
            }) { Text(if (index < questions.lastIndex) "Question suivante" else "Recommencer") }
        }
        Text("Score : $score / ${if (submitted) index + 1 else index}", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun CasesScreen() {
    SectionTitle("Cas pratiques", "Méthode de résolution")
    Panel {
        Text("Cas pratique — responsabilité civile", fontWeight = FontWeight.Bold)
        Text("Un conducteur cause un dommage matériel à un tiers. La victime souhaite obtenir réparation. Présentez les conditions de mise en jeu de la responsabilité et les éléments à établir.")
        Text("Méthode suggérée", fontWeight = FontWeight.SemiBold)
        Text("1. Qualifier juridiquement les faits.\n2. Identifier le fondement juridique applicable.\n3. Examiner les conditions : fait générateur, dommage et lien de causalité.\n4. Discuter les causes d'exonération.\n5. Conclure de manière motivée.")
        var answer by remember { mutableStateOf("") }
        OutlinedTextField(answer, { answer = it }, label = { Text("Ta réponse / ton plan") }, modifier = Modifier.fillMaxWidth(), minLines = 5)
        Text("Conseil : cite les textes applicables et distingue les faits établis des hypothèses.", style = MaterialTheme.typography.bodySmall)
    }
    Panel {
        Text("Important", fontWeight = FontWeight.Bold)
        Text("Cet exercice est un support d'entraînement fictif. Il ne constitue pas un corrigé officiel.")
    }
}

@Composable
fun JurisprudenceScreen() {
    SectionTitle("Jurisprudence", "Fiches de travail à compléter et vérifier")
    val entries = listOf(
        "Droit civil" to "Responsabilité — conditions de réparation, dommage et causalité.",
        "Droit pénal" to "Légalité criminelle — interprétation stricte de la loi pénale.",
        "Procédure civile" to "Contradictoire — respect des droits de la défense et débat loyal.",
        "Droit administratif" to "Légalité — contrôle de conformité de l'action administrative au droit."
    )
    entries.forEach { (area, summary) ->
        Panel {
            Text(area, fontWeight = FontWeight.Bold)
            Text(summary)
            Text("Référence exacte à rechercher dans les bases officielles avant mémorisation.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PlanningScreen() {
    SectionTitle("Planning de révision", "Coche les tâches terminées")
    val tasks = listOf("Réviser les obligations", "Faire 20 QCM de droit pénal", "Rédiger un cas pratique", "Relire une fiche de procédure civile", "Faire une synthèse de jurisprudence")
    tasks.forEach { task ->
        var checked by remember(task) { mutableStateOf(false) }
        Panel {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked, { checked = it })
                Text(task, modifier = Modifier.weight(1f))
            }
        }
    }
    Text("Astuce : répartis les matières sur la semaine et réserve un créneau régulier aux annales.", style = MaterialTheme.typography.bodySmall)
}

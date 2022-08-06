import javafx.application.Application
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.*
import javafx.stage.Stage
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.file.Files


class Main : Application() {
    override fun start(stage: Stage) {
        // CREATE WIDGETS TO DISPLAY
        // menubar & toolbar
        val menuBar = MenuBar()
        val fileMenu = Menu("File")
        val viewMenu = Menu("View")
        val actionsMenu = Menu("Actions")
        val optionsMenu = Menu("Options")


        val menuQuit = MenuItem("Quit")
        val menuPrev = MenuItem("Prev")
        val menuNext = MenuItem("Next")
        val menuHome = MenuItem("Home")
        val menuRename = MenuItem("Rename")
        val menuMove = MenuItem("Move")
        val menuDelete = MenuItem("Delete")
        val extraToggle = RadioMenuItem("Show Hidden Files")
        menuQuit.setOnAction { Platform.exit() }
        actionsMenu.items.addAll(menuHome, menuPrev,menuNext, menuRename, menuMove, menuDelete )
        fileMenu.items.addAll(menuQuit)
        optionsMenu.items.addAll(extraToggle)
        menuBar.menus.addAll(fileMenu, viewMenu, actionsMenu, optionsMenu)


        val toolbar = ToolBar()
        val button1 = Button("Home")
        button1.graphic = ImageView(Image("home.png"))
        val button2 = Button("Prev")
        button2.graphic = ImageView(Image("prev.png"))
        val button3 = Button("Next")
        button3.graphic = ImageView(Image("next.png"))
        val button4 = Button("Delete")
        button4.graphic = ImageView(Image("delete.png"))
        val button5 = Button("Rename")
        button5.graphic = ImageView(Image("rename.png"))
        val button6 = Button("Move")
        button6.graphic = ImageView(Image("move.png"))
        toolbar.items.addAll(button1, button2, button3, button4, button6, button5)

        // stack menu and toolbar in the top region
        val vbox = VBox(menuBar, toolbar)
        vbox.padding = Insets(5.0)
        //val bbox = Vbox(toolbar)
        // panel on left side, need to replace with a tree
        val pane = Pane()
        pane.minWidth = 0.0
        pane.prefHeight = 600.0
        pane.style = "-fx-background-color: white;"

        val right = Pane()
        right.padding = Insets(5.0)
        right.minWidth = 0.0
        pane.style = "-fx-background-color: white;"
        val bottom = BorderPane()
        bottom.padding = Insets(5.0)

        val center = BorderPane()
        center.minWidth = 0.0
        center.minHeight = 0.0

        bottom.padding = Insets(5.0)
        /******************************************************************************/

        // dir: array of file

        // homeposition record the path to the home
        val homepathname = "${System.getProperty("user.dir")}/test"
        var curpathname = homepathname
        var dir = File(curpathname).listFiles()
        var filenames = showdirec(dir, 0)
        val list = ListView<String>()
        list.items = FXCollections.observableArrayList(filenames)
        var insets = Insets(5.0)

        val border = BorderPane()
        border.padding = insets
        border.top = vbox
        border.center = center
        border.right = right
        border.bottom = bottom
        right.minWidth = 10.0
        border.padding = insets
        StackPane(list).minHeight = 0.0
        StackPane(list).minWidth = 0.0
        border.left = StackPane(list)
        var lable = Label()
        var sign = 0
        list.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            try {
                if (File("${File(curpathname).absolutePath}/" + newValue).isFile) {
                    var detect = shootype(newValue)
                    if (detect == 1) {
                        center.children.clear()
                        val stream = FileInputStream("${File(curpathname).absolutePath}/" + newValue)
                        var image = ImageView(Image(stream))
                        image.fitWidthProperty().bind(center.widthProperty())
                        image.fitHeightProperty().bind(center.heightProperty())
                        center.center = image
                    } else if (detect == 2) {
                        center.children.clear()
                        val bufferedReader: BufferedReader =
                            File("${File(curpathname).absolutePath}/" + newValue).bufferedReader()
                        val inputString = bufferedReader.use { it.readText() }
                        val scroll = ScrollPane()
                        val textFlowPane = TextFlow()
                        textFlowPane.setLineSpacing(5.0)
                        textFlowPane.setTextAlignment(TextAlignment.LEFT)
                        textFlowPane.setLineSpacing(5.0)
                        var text = Text(inputString)
                        textFlowPane.children.add(text)
                        scroll.content = textFlowPane
                        scroll.viewportBoundsProperty().addListener { _, _, newValue ->
                            textFlowPane.setPrefSize(newValue.width, newValue.height)
                        }
                        center.center = scroll
                    } else {
                        center.children.clear()
                        val header1 = Font.font("Verdana", FontWeight.BOLD, 20.0)
                        val header1colour = Color.RED
                        val text1 = Text("Unsupported type")
                        text1.font = header1
                        text1.fill = header1colour
                        val textFlowPane1 = TextFlow()
                        textFlowPane1.children.addAll(text1)
                        center.center = textFlowPane1
                    }
                    border.padding = insets
                    lable = Label(curpathname + "/" + list.selectionModel.selectedItem)
                    bottom.left = lable
                } else if(File("${File(curpathname).absolutePath}" + newValue).isDirectory){
                    if(!File("${File(curpathname).absolutePath}" + newValue).canRead()){
                        center.children.clear()
                        val header1 = Font.font("Verdana", FontWeight.BOLD, 20.0)
                        val header1colour = Color.RED
                        val text1 = Text("This directory cannot be read")
                        text1.font = header1
                        text1.fill = header1colour
                        val textFlowPane1 = TextFlow()
                        textFlowPane1.children.addAll(text1)
                        center.center = textFlowPane1
                    }else{
                        center.children.clear()
                    }
                    lable = Label(curpathname + list.selectionModel.selectedItem)
                }else{
                    lable = Label(curpathname + "/")
                }
                bottom.left = lable
            } catch (e : IOException){
                center.children.clear()
                val header1 = Font.font("Verdana", FontWeight.BOLD, 20.0)
                val header1colour = Color.RED
                val text1 = Text("File cannot be read")
                text1.font = header1
                text1.fill = header1colour
                val textFlowPane1 = TextFlow()
                textFlowPane1.children.addAll(text1)
                center.center = textFlowPane1
                lable = Label(curpathname + "/" +list.selectionModel.selectedItem)
                bottom.left = lable
            }
            menuHome.setOnAction {
                curpathname = homepathname
                filenames = showdirec(File(curpathname).listFiles(), sign)
                list.items = FXCollections.observableArrayList(filenames)
                list.selectionModel.selectIndices(0)
            }

            menuPrev.setOnAction {
                if (curpathname != homepathname) {
                    var prev = curpathname.substringBeforeLast('/')
                    filenames = showdirec(File(prev).listFiles(),sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.selectIndices(0)
                    curpathname = prev
                    lable = Label(curpathname + list.selectionModel.selectedItem)
                    bottom.left = lable
                    list.selectionModel.selectIndices(0)
                }
            }

            menuNext.setOnAction {
                if (File("${File(curpathname).absolutePath}/" + newValue).isDirectory) {
                    curpathname = "$curpathname$newValue"
                    dir = File(curpathname).listFiles()
                    filenames = showdirec(dir,sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.selectIndices(0)
                }
            }

            menuDelete.setOnAction {
                val confirmation = Alert(Alert.AlertType.CONFIRMATION)
                confirmation.title = "Delete Confirmation"
                confirmation.contentText = "Do you want to delete this file: " + list.selectionModel.selectedItem + "?"
                val result1 = confirmation.showAndWait()
                if (result1.isPresent) {
                    try {
                        when (result1.get()) {
                            ButtonType.OK -> {
                                if (File(curpathname + list.selectionModel.selectedItem).isDirectory) {
                                    File(curpathname + list.selectionModel.selectedItem).deleteRecursively()
                                } else {
                                    File(curpathname + "/" + list.selectionModel.selectedItem).delete()
                                }
                                center.children.clear()
                                dir = File(curpathname).listFiles()
                                filenames = showdirec(dir,sign)
                                list.items = FXCollections.observableArrayList(filenames)
                                list.selectionModel.selectIndices(0)
                                lable = Label(curpathname + list.selectionModel.selectedItem)
                            }
                            ButtonType.CANCEL -> {
                                list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                            }
                        }
                    }catch (e : IOException){
                        val alert = Alert(Alert.AlertType.ERROR)
                        alert.title = "Warning"
                        alert.contentText = "This is invalid."
                        alert.showAndWait()
                    }
                }
            }

            menuRename.setOnAction {
                val curpath: String
                val confirmation = Alert(Alert.AlertType.CONFIRMATION)
                confirmation.title = "Confirmation"
                confirmation.contentText = "Do you want to rename this file: " + list.selectionModel.selectedItem + "?"
                val result1 = confirmation.showAndWait()
                if (result1.isPresent) {
                    try{
                        when (result1.get()) {
                            ButtonType.OK -> {
                                val dialog = TextInputDialog("")
                                dialog.title = "Rename"
                                dialog.headerText = "Enter the new file name."
                                val result = dialog.showAndWait()
                                if (result.isPresent) {
                                    if (File(curpathname +list.selectionModel.selectedItem).isDirectory) {
                                        curpath = curpathname + list.selectionModel.selectedItem
                                    } else {
                                        curpath = curpathname + "/" + list.selectionModel.selectedItem
                                    }
                                    val resultname = result.toString().substring(9, result.toString().length - 1)
                                    val newpath = curpath.substringBeforeLast("/") + "/" + resultname
                                    Files.move(File(curpath).toPath(), File(newpath).toPath())
                                    val mypath = newpath.substring(0, newpath.length - resultname.length - 1)
                                    val mylist = File(mypath).listFiles()
                                    val myshow = showdirec(mylist, sign)
                                    list.items = FXCollections.observableArrayList(myshow)
                                    if (File(mypath).isDirectory) {
                                        list.selectionModel.select("/" + resultname)
                                    } else {
                                        list.selectionModel.select(resultname)
                                    }
                                    lable = Label(curpathname + list.selectionModel.selectedItem)
                                    bottom.left = lable
                                    if (File(curpathname + list.selectionModel.selectedItem).isDirectory) {
                                        list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                                    } else {
                                        list.selectionModel.select(
                                            list.items.indexOf(
                                                list.selectionModel.selectedItem.substring(
                                                    1,
                                                    list.selectionModel.selectedItem.length
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                            ButtonType.CANCEL -> {
                                list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                            }
                        }
                    }catch (e : IOException){
                        val alert = Alert(Alert.AlertType.ERROR)
                        alert.title = "Warning"
                        alert.contentText = "This is invalid."
                        alert.showAndWait()
                    }
                }
            }
            menuMove.setOnAction {
                val confirmation = Alert(Alert.AlertType.CONFIRMATION)
                confirmation.title = "Confirmation dialog"
                confirmation.contentText = "Do you want to move this file: " + list.selectionModel.selectedItem + "?"
                val result1 = confirmation.showAndWait()
                if (result1.isPresent) {
                    when (result1.get()) {
                        ButtonType.OK -> {
                            val dialog = TextInputDialog("")
                            dialog.title = "Enter value"
                            dialog.headerText = "Enter the path of directory you want to move into"
                            val result = dialog.showAndWait()
                            if (result.isPresent) {
                                try {
                                    val myresult = result.toString().substring(9, result.toString().length - 1)
                                    val destination = File(myresult)
                                    var myselect = list.selectionModel.selectedItem
                                    if (destination.isDirectory) {
                                        if (File(curpathname +list.selectionModel.selectedItem).isDirectory) {
                                            myselect = list.selectionModel.selectedItem.substring(
                                                1,
                                                list.selectionModel.selectedItem.length
                                            )
                                        }
                                        Files.move(
                                            File(curpathname + "/" + myselect).toPath(),
                                            File(myresult + "/" + myselect).toPath()
                                        )
                                        curpathname = myresult
                                        filenames = showdirec(destination.listFiles(),sign)
                                        list.items = FXCollections.observableArrayList(filenames)
                                        list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                                        lable = Label(curpathname +"/"+myselect)
                                    } else {
                                        val alert = Alert(Alert.AlertType.ERROR)
                                        alert.title = "Warning"
                                        alert.contentText = "This is invalid path."
                                        alert.showAndWait()
                                    }
                                }catch (e : IOException){
                                    val alert = Alert(Alert.AlertType.ERROR)
                                    alert.title = "Warning"
                                    alert.contentText = "This is invalid."
                                    alert.showAndWait()
                                }
                            }
                        }
                        ButtonType.CANCEL -> {
                            list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                        }
                    }
                }
            }

            //home button
            button1.setOnAction {
                curpathname = homepathname
                filenames = showdirec(File(curpathname).listFiles(),sign)
                list.items = FXCollections.observableArrayList(filenames)
                list.selectionModel.selectIndices(0)
            }

            //prev button
            button2.setOnAction {
                if (curpathname != homepathname) {
                    val prev = curpathname.substringBeforeLast('/')
                    filenames = showdirec(File(prev).listFiles(),sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.selectIndices(0)
                    curpathname = prev
                    lable = Label(curpathname + list.selectionModel.selectedItem)
                    bottom.left = lable
                }
            }

            //next button
            button3.setOnAction {
                if (File("${File(curpathname).absolutePath}/" + newValue).isDirectory) {
                    curpathname = "$curpathname$newValue"
                    dir = File(curpathname).listFiles()
                    filenames = showdirec(dir,sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.selectIndices(0)
                }
            }

            //delete button
            button4.setOnAction {
                val confirmation = Alert(Alert.AlertType.CONFIRMATION)
                confirmation.title = "Delete Confirmation"
                confirmation.contentText = "Do you want to delete this file: " + list.selectionModel.selectedItem + "?"
                val result1 = confirmation.showAndWait()
                if (result1.isPresent) {
                    try {
                        when (result1.get()) {
                            ButtonType.OK -> {
                                if (File(curpathname + list.selectionModel.selectedItem).isDirectory) {
                                    File(curpathname + list.selectionModel.selectedItem).deleteRecursively()
                                } else {
                                    File(curpathname + "/" + list.selectionModel.selectedItem).delete()
                                }
                                center.children.clear()
                                dir = File(curpathname).listFiles()
                                filenames = showdirec(dir,sign)
                                list.items = FXCollections.observableArrayList(filenames)
                                list.selectionModel.selectIndices(0)
                                lable = Label(curpathname + list.selectionModel.selectedItem)
                            }
                            ButtonType.CANCEL -> {
                                list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                            }
                        }
                    }catch (e : IOException){
                        val alert = Alert(Alert.AlertType.ERROR)
                        alert.title = "Warning"
                        alert.contentText = "This is invalid."
                        alert.showAndWait()
                    }
                }
            }

            //rename button
            button5.setOnAction {
                var curpath: String
                val confirmation = Alert(Alert.AlertType.CONFIRMATION)
                confirmation.title = "Confirmation"
                confirmation.contentText = "Do you want to rename this file: " + list.selectionModel.selectedItem + "?"
                val result1 = confirmation.showAndWait()
                if (result1.isPresent) {
                    try{
                    when (result1.get()) {
                        ButtonType.OK -> {
                            val dialog = TextInputDialog("")
                            dialog.title = "Rename"
                            dialog.headerText = "Enter the new file name."
                            val result = dialog.showAndWait()
                            if (result.isPresent) {
                                if (File(curpathname +list.selectionModel.selectedItem).isDirectory) {
                                    curpath = curpathname + list.selectionModel.selectedItem
                                } else {
                                    curpath = curpathname + "/" + list.selectionModel.selectedItem
                                }
                                val resultname = result.toString().substring(9, result.toString().length - 1)
                                var newpath = curpath.substringBeforeLast("/") + "/" + resultname
                                Files.move(File(curpath).toPath(), File(newpath).toPath())
                                var mypath = newpath.substring(0, newpath.length - resultname.length - 1)
                                var mylist = File(mypath).listFiles()
                                var myshow = showdirec(mylist,sign)
                                list.items = FXCollections.observableArrayList(myshow)
                                if (File(mypath).isDirectory) {
                                    list.selectionModel.select("/" + resultname)
                                } else {
                                    list.selectionModel.select(resultname)
                                }
                                lable = Label(curpathname + list.selectionModel.selectedItem)
                                bottom.left = lable
                                if (File(curpathname + list.selectionModel.selectedItem).isDirectory) {
                                    list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                                } else {
                                    list.selectionModel.select(
                                        list.items.indexOf(
                                            list.selectionModel.selectedItem.substring(
                                                1,
                                                list.selectionModel.selectedItem.length
                                            )
                                        )
                                    )
                                }
                            }
                        }
                        ButtonType.CANCEL -> {
                            list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                        }
                    }
                }catch (e : IOException){
                        val alert = Alert(Alert.AlertType.ERROR)
                        alert.title = "Warning"
                        alert.contentText = "This is invalid."
                        alert.showAndWait()
                    }
                }
            }

            //move button
            button6.setOnAction {
                // confirmation
                val confirmation = Alert(Alert.AlertType.CONFIRMATION)
                confirmation.title = "Confirmation dialog"
                confirmation.contentText = "Do you want to move this file: " + list.selectionModel.selectedItem + "?"
                val result1 = confirmation.showAndWait()
                if (result1.isPresent) {
                    when (result1.get()) {
                        ButtonType.OK -> {
                            val dialog = TextInputDialog("")
                            dialog.title = "Enter value"
                            dialog.headerText = "Enter the path of directory you want to move into"
                            val result = dialog.showAndWait()
                            if (result.isPresent) {
                                try {
                                    var myresult = result.toString().substring(9, result.toString().length - 1)
                                    var destination = File(myresult)
                                    var myselect = list.selectionModel.selectedItem
                                    if (destination.isDirectory) {
                                        if (File(curpathname +list.selectionModel.selectedItem).isDirectory) {
                                            myselect = list.selectionModel.selectedItem.substring(
                                                1,
                                                list.selectionModel.selectedItem.length
                                            )
                                        }
                                        Files.move(
                                            File(curpathname + "/" + myselect).toPath(),
                                            File(myresult + "/" + myselect).toPath()
                                        )
                                        curpathname = myresult
                                        filenames = showdirec(destination.listFiles(),sign)
                                        list.items = FXCollections.observableArrayList(filenames)
                                        list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                                        lable = Label(curpathname +"/"+myselect)
                                    } else {
                                        val alert = Alert(Alert.AlertType.ERROR)
                                        alert.title = "Warning"
                                        alert.contentText = "This is invalid path."
                                        alert.showAndWait()
                                    }
                                }catch (e : IOException){
                                    val alert = Alert(Alert.AlertType.ERROR)
                                    alert.title = "Warning"
                                    alert.contentText = "This is invalid."
                                    alert.showAndWait()
                                }
                            }
                        }
                        ButtonType.CANCEL -> {
                            list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                        }
                    }
                }

            }

            extraToggle.setOnAction {
                var filelist: MutableList<File> = mutableListOf(File(homepathname))
                filelist = getallfiles(File(homepathname))
                for(file in filelist){
                    if(file.name != "") {
                        if (!file.isHidden && file.name[0] == '.') {
                            Files.setAttribute(file.toPath(), "dos:hidden", true)
                        }
                    }
                }
                if(extraToggle.isSelected) {
                    sign = 1
                    filenames = showdirec(File(curpathname).listFiles(),sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.selectIndices(0)
                }else{
                    sign = 0
                    filenames = showdirec(File(curpathname).listFiles(),sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.select(list.items.indexOf(list.selectionModel.selectedItem))
                }

            }

            list.setOnKeyPressed {event ->
                if (event.code.equals(KeyCode.ENTER) && File("${File(curpathname).absolutePath}/" + newValue).isDirectory) {
                    curpathname = "$curpathname$newValue"
                    dir = File(curpathname).listFiles()
                    filenames = showdirec(dir,sign)
                    list.items = FXCollections.observableArrayList(filenames)
                    list.selectionModel.selectIndices(0)
                }else if ((event.code.equals(KeyCode.DELETE)||event.code.equals(KeyCode.BACK_SPACE))){
                    if(curpathname != homepathname){
                        var prev = curpathname.substringBeforeLast('/')
                        filenames = showdirec(File(prev).listFiles(),sign)
                        list.items = FXCollections.observableArrayList(filenames)
                        curpathname = prev
                        lable = Label(curpathname + list.selectionModel.selectedItem)
                        bottom.left = lable
                        list.selectionModel.selectIndices(0)
                    }
                }
            }
            //double-click next
                list.setOnMouseClicked { event ->
                    if ((event.clickCount == 2 && File("${File(curpathname).absolutePath}/" + newValue).isDirectory)) {
                        if (File("${File(curpathname).absolutePath}" + newValue).canRead()) {
                            curpathname = "$curpathname$newValue"
                            dir = File(curpathname).listFiles()
                            filenames = showdirec(dir,sign)
                            list.items = FXCollections.observableArrayList(filenames)
                            list.selectionModel.selectIndices(0)
                        }else{
                            center.children.clear()
                            val header1 = Font.font("Verdana", FontWeight.BOLD, 20.0)
                            val header1colour = Color.RED
                            val text1 = Text("Cannot get into this directory")
                            text1.font = header1
                            text1.fill = header1colour
                            val textFlowPane1 = TextFlow()
                            textFlowPane1.children.addAll(text1)
                            center.center = textFlowPane1
                        }
                    }
                }
        }
            // CREATE AND SHOW SCENE
            val scene = Scene(border, 800.0, 600.0)
            stage.scene = scene
            stage.title = "File Browser"
            stage.show()
    }

        /* show all the file in the current directory*/
        private fun showdirec(dir: Array<File>, sign: Int): MutableList<String> {
            val filenames: MutableList<String> = mutableListOf<String>()
            if(sign == 0){
                for (file in dir) {
                    if (file.isFile && file.name != ".DS_Store") {
                        filenames.add(file.name)
                    }
                    if (file.isDirectory) {
                        filenames.add("/" + file.name)
                    }
                }
            }else {
                for (file in dir) {
                    if (!file.isHidden && file.isFile && file.name != ".DS_Store") {
                        filenames.add(file.name)
                    }
                    if (file.isDirectory && !file.isHidden) {
                        filenames.add("/" + file.name)
                    }
                }
            }
            filenames.sort()
            return filenames
        }

        private fun getallfiles(file: File): MutableList<File>{
            val mylist = mutableListOf<File>(File(""))
            if(file.listFiles().isEmpty()){
                return mylist
            }
            for(list in file.listFiles()){
                if(list.isFile){
                    mylist.add(list)
                }else{
                    mylist.add(list)
                    mylist.addAll(getallfiles(list))
                }
            }
            return mylist
        }
        private fun shootype(name : String): Int{
        var type = name.substringAfterLast(".")
            if( type == "png" || type =="jpg" || type == "bmp"){
                return 1
            }else if(type == "txt" || type == "md"){
                return 2
            }else{
                return 0
            }
    }
    }
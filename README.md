
<h1>Doucument Reader</h1>

<h2>1.The left-hand side will display a list of files and directories.</h2>

  •When launched, this list will show the contents of a test folder

<h2>2.The user can use navigate through this list using the keyboard, toolbar buttons, or menu options.</h2>

  • User are be able to <ins>**scroll up and down**</ins> the list of files using arrow keys to change the selected file, or pick an entry in the list by clicking on it with a pointing device. The status line always display the path and filename of the selected item.

  • Users are able to <ins>**descend into a directory**</ins>, using a hotkey (Enter), or a toolbar button (Next), or a menu item, or by double-clicking with a pointing device.

  • Users are able to <ins>**navigate up to the parent directory**</ins> using a hotkey (Backspace or Delete), or a toolbar button (Prev), or a menu item.

  • Users are able to <ins>**return to the Home directory**</ins> at any time by pressing a button on the toolbar or selecting a corresponding menu item.
  
<img width="500" alt="截屏2022-08-06 下午3 59 29" src="https://user-images.githubusercontent.com/85118325/183264478-c38756f6-9d64-4a7c-8f49-967c72958c3c.png">

<h2>3.User can perform file-manipulation operations:</h2>

• Users are able to <ins>**rename a file or directory**</ins>, by selecting the item, and then pressing a rename icon on the toolbar, or selecting Action - Rename from the menu bar. You should prompt the user for the new name. If they provide an invalid name, the error and cancel the rename operation will be displayed 

<img width="500" alt="截屏2022-08-06 下午3 58 57" src="https://user-images.githubusercontent.com/85118325/183264484-5aefaaa2-581d-4976-9636-36b843523fff.png">

  •Users are able to <ins>**move a file or directory**</ins> to a different location, by selecting an item, and then pressing a move icon on the toolbar, or selecting Action - Move from the menu bar. You should prompt the user for the destination directory with a dialog box, and display an error if the destination is invalid.
  
 <img width="500" alt="截屏2022-08-06 下午4 03 12" src="https://user-images.githubusercontent.com/85118325/183264486-387aa218-865b-4f9b-ab79-54b9fc3a2988.png">

  •Users are able to <ins>**delete a file or directory**</ins>, by selecting the item, and then pressing a delete icon on the toolbar, or selecting Action - Delete from the menu bar. You must prompt the user with a confirmation dialog before proceeding (i.e. ask them if they wish to delete the item, and give them a chance to cancel this operation).

<h2>4.User can show or hide hidden files:</h2>

•There should be a toggle option on the menubar Option - <ins>**Show Hidden Files**</ins> that determines if hidden files are shown or not. It should default to hiding hidden files. Changing this option should immediately be reflected in the file list, and should persist as the user navigates.

<h2>5.The right-hand side will display the content of the selected file:</h2>

  •If a directory is selected, you should show a blank page for the content pane.

  •If the selected file is an image

  •If the selected file is text, then the contents of the file will be displayed. The contents pane should have a scrollbar if the file requires more than a single screen to display.

  •If the file is any other type, or an unknown type, the contents pane will display the text “Unsupported type”.

  •If the file is unreadable for any reason, it will show display the text “File cannot be read” in the contents pane.






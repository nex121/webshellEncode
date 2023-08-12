package com.we.webshellencode.controller;

import com.we.webshellencode.util.FileUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class MainController {
    @FXML
    public TreeView<String> coding_method;
    @FXML
    private TextField select_file_path;
    @FXML
    private TextArea webshell_content;
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    String selectedNodeValue = "";

    public void initialize() {
        TreeItem<String> rootItem = new TreeItem<>();
        TreeItem<String> javaItem = new TreeItem<>("java");
//        TreeItem<String> phpItem = new TreeItem<>("php");
//        TreeItem<String> netItem = new TreeItem<>(".net");

        rootItem.getChildren().add(javaItem);
//        rootItem.getChildren().add(phpItem);
//        rootItem.getChildren().add(netItem);

        //java 编码节点列表
        List<String> encodings = Arrays.asList(
                "unicode",
                "cp273", "cp037", "Cp273", "Cp277", "Cp278", "Cp285", "Cp280",
                "Cp284", "cp297", "cp420", "cp424", "cp500", "Cp838", "Cp870",
                "Cp871", "cp875", "cp939", "Cp933", "Cp935", "Cp937", "cp1026",
                "cp1140", "UTF-16BE", "UTF-16LE"
        );

        for (String encoding : encodings) {
            String name = encoding.toLowerCase();
            javaItem.getChildren().add(new TreeItem<>(name));
        }

        coding_method.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selectedNodeValue = newValue.getValue());

        coding_method.setShowRoot(false);
        coding_method.setRoot(rootItem);
        coding_method = new TreeView<>(rootItem);

    }

    @FXML
    private void about_tool() {
        alert.setTitle("by nex121");
        alert.setHeaderText("工具提示");
        alert.setContentText("本工具只供研究，禁止非法使用！");
        alert.showAndWait();
    }

    @FXML
    private void select_file() {
        Stage stage = (Stage) select_file_path.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select URL File");
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            this.select_file_path.setText(file.getAbsolutePath());
            String fileContent = FileUtil.ReadFileContent(file.getAbsolutePath());
            this.webshell_content.setText(fileContent);
        }
    }

    @FXML
    private void gen_shell() {
        String contentEncode = selectedNodeValue.trim();
        String encode = "<?xml version=\"1.0\" encoding=\"" + contentEncode + "\"?>";

        Stage stage = (Stage) select_file_path.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("保存文件");
        fileChooser.setInitialFileName("webshellEncode_" + contentEncode + ".jsp");
        File file = fileChooser.showSaveDialog(stage);
        String data = webshell_content.getText();

        try (FileOutputStream output = new FileOutputStream(file)) {
            if (contentEncode.equals("unicode")) {
                data = FileUtil.txt2Unicode(data);
                byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
                output.write(bytes);
                alert.setHeaderText("生成成功！");
                alert.showAndWait();
                return;
            }
            data = encode + data;
            byte[] bytes = data.getBytes(contentEncode);
            output.write(bytes);
            alert.setHeaderText("生成成功！");
            alert.showAndWait();
        } catch (Exception e) {
            alert.setHeaderText("生成失败！");
            alert.showAndWait();
        }
    }
}


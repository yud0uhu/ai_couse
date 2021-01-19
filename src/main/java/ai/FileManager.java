//package ai;
//
//import java.io.IOException;
//import java.net.URI;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Objects;
//
///**
// * クラスパスからファイルを読み込むライブラリ用のクラス.
// *
// * @author yamakawa@photon.chitose.ac.jp
// */
//public class FileManager {
//
//    private Path path;
//
//    /**
//     * 新たなインスタンスを作成する引数付きコンストラクタ.
//     *
//     * @param fileName 読み込むファイル名.
//     * @throws IllegalArgumentException もしファイル名のファイルが存在しなければスローする.
//     */
//    public FileManager(String fileName) {
//        this.path = makePath(fileName);
//    }
//
//    /**
//     * pathフィールドを使って、ファイルパスから全行を読み込む.
//     *
//     * @return ファイルの行を要素とした文字列型の配列.
//     * @throws IOException もしファイルが読み込めなければスローする.
//     */
//    public String[] getAsArray() throws IOException {
//        return Files.readAllLines(path)
//                .toArray(new String[0]);
//    }
//
//    /**
//     * pathフィールドを使って、ファイルパスから全行を読み込む.
//     *
//     * @return ファイルの行を要素とした文字列型のList.
//     * @throws IOException もしファイルが読み込めなければスローする.
//     */
//    public List<String> getAsList() throws IOException {
//        return Files.readAllLines(path);
//    }
//
//    /**
//     * ファイル名から作成したパスをPath型で返すå.
//     *
//     * @param fileName 読み込むファイル名. nullではない前提.
//     * @return ファイルのパス. クラスファイルと同じパッケージに限定している.
//     * @throws IllegalArgumentException もしファイル名のファイルが存在しなければスローする.
//     */
//    private Path makePath(String fileName) {
//        var relative = "./" + fileName;
//        var url = this.getClass().getResource(relative);
//        if (Objects.isNull(url)) {
//            throw new IllegalArgumentException(fileName + "は存在しない");
//        }
//        var urlStr = url.toString();
//        var uri = URI.create(urlStr);
//        return Paths.get(uri);
//    }
//
//}

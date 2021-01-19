package ai;

import java.util.Arrays;
import java.util.Random;

// 文字認識を行う
public class NeuralNet {

	final int N_INPUT;  // 入力層の数
	final int N_HIDDEN; // 中間層の数
	final int N_OUTPUT;  // 出力層の数

	double w1[][];	//入力層>隠れ層の重み
	double w2[][];	//隠れ層>出力層の重み
	double b1[];	//定数1を入力とした入力層>隠れ層の重み 数式的には閾値θとした時の-θと等しい
	double b2[];	//定数1を入力とした隠れ層>出力層の重み 数式的には閾値θとした時の-θと等しい

	double input[];  // 入力層
	double hidden[]; // 中間層
	double output[]; // 出力層

	double alpha = 0.1;	//学習率

	public NeuralNet( int nInput, int nHidden, int nOutput ) {
		N_INPUT = nInput;
		N_HIDDEN = nHidden;
		N_OUTPUT = nOutput;

		input  = new double[N_INPUT];  // 入力層
		hidden = new double[N_HIDDEN]; // 中間層
		output = new double[N_OUTPUT]; // 出力層

		// 重みを-0.1~0.1で初期化
		Random rnd = new Random();

		w1 = new double[N_INPUT][N_HIDDEN];
		for(int i=0; i<N_INPUT; i++){
			for(int j=0; j<N_HIDDEN; j++){
				w1[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}
		b1 = new double[N_HIDDEN];

		w2 = new double[N_HIDDEN][N_OUTPUT];
		for(int i=0; i<N_HIDDEN; i++){
			for(int j=0; j<N_OUTPUT; j++){
				w2[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}
		b2 = new double[N_OUTPUT];

	}

	// NNに入力し、出力を計算する
	public double[] compute(double x[]){

		// 入力層の入力
		for(int i=0; i<N_INPUT; i++){
			input[i] = x[i];
		}

/**
 * 二クラス分類問題では、
 * 一般的に隠れ層(=中間層)の活性化関数にReLU関数f(x)=max(0,x)f(x)=max(0,x)
 * 出力層の活性化関数に標準シグモイド関数f(x)=11+e−xf(x)=11+e−x
 * 誤差関数に交差エントロピー関数Ecross=−∑ktklogyk+(1−tk)log(1−yk)Ecross=−∑ktklogyk+(1−tk)log(1−yk)
 * を用いる。
 * そのため、中間層の活性化関数をReLU(ランプ)関数での実装に置き換えた。
 */
/**
 * 考察
 * シグモイド関数+シグモイド関数での実装⇒step:31300で学習終了
 * シグモイド関数+ランプ関数での実装⇒step:26800で学習終了
 * ランプ関数+ソフトマックス関数での実装⇒step:29300で学習終了
 * 学習効率はランプ関数の方がよいと推察できる。
 */

		// 中間層の計算
		for(int i=0; i<N_HIDDEN; i++){
			hidden[i] = 0.0;
			for(int j=0; j<N_INPUT; j++){
				hidden[i] += w1[j][i] * input[j];
			}
			hidden[i] += b1[i];
//			hidden[i] = sigmoid(hidden[i]);
			hidden[i] = relu(hidden[i]);
		}

		// 出力層の計算
		for(int i=0; i<N_OUTPUT; i++){
			output[i] = 0.0;
			for(int j=0; j<N_HIDDEN; j++){
				output[i] += w2[j][i] * hidden[j]; // 重み*第2層の出力結果
			}
			output[i] += b2[i];
//			output[i] = sigmoid(output[i]);

//			double[] a = new double[]{};
//			for (int j=0; j<a.length; j++){
//				a[j] = output[j];
//			}

			double[] result = softmax(output);
			output[i] = result[i];

			// ソフトマックス関数の実装
			//INDArray a = Nd4j.create(new double[] {output[i]});
			//output = softmax(a);

		}

		return output;
	}

	public static int calcMax(int[] array) {

		int intMax = array[0];

		for (int i = 1; i < array.length; i++ ) {
			if(intMax < array[i]) {
				intMax = array[i];
			}
		}
		return intMax;
	}


// ⑧誤差伝搬 中間層 実装例は活性化関数をシグモイドで実装している

//	public double[] softmax(INDArray x) {
//
//		// 指数関数
//		INDArray exp_a = Transforms.exp(x);
//		// 指数関数の和
//		Number sum_exp_a = exp_a.sumNumber();
//
//		// ソフトマックス関数
//		INDArray y = exp_a.div(sum_exp_a);
//		double [] a = new double[]{};
//		for (int i = 0; i<y.columns(); i++) {
//			a[i] = y.reshape(6,6).getDouble();
//		}
//		return a;
//	}

//	 シグモイド関数
	public double sigmoid(double i){
		double a = 1.0 / (1.0 + Math.exp(-i));
		return a;
	}

	// ランプ関数 h(x) = {x(x>0),0(x<=0)}
	// 入力が0以下なら0、0以上ならそのまま出力する
	public double relu(double i) {
		if ( i > 0.0 ) return i;
		else return 0.0;
	}

	// ソフトマックス関数
	public double[] softmax(double[] x){
		double maxValue = Arrays.stream(x).max().getAsDouble();
		double[] value = Arrays.stream(x).map(y-> Math.exp(y - maxValue)).toArray(); // maxValue:任意定数C オーバーフロー対策
		double total = Arrays.stream(value).sum();

		return Arrays.stream(value).map(p -> p/total).toArray();
	}

	public double[][] softmax(double[][] x){
		double[][] result = new double[x.length][];
		double[] a = new double[]{};
		for (int i = 0; i < result.length; i++){
			result[i] = softmax(x[i]);
		}
		return result;
	}


	// 誤差逆伝播法による重みの更新
	public void backPropagation(double teach[]){

		// 誤差
		double[] deltas = new double[N_OUTPUT];

		// 中間層>出力層の重みを更新
		for(int j=0; j<N_OUTPUT; j++){
			deltas[j] = (teach[j]-output[j]) * output[j] * (1.0-output[j]);
			for(int i=0; i<N_HIDDEN; i++){
				w2[i][j] += alpha * deltas[j] * hidden[i];
			}
			b2[j] += alpha * deltas[j];
		}

		// 入力層>中間層の重みを更新
		for(int i=0; i<N_HIDDEN; i++){

			double sum = 0.0;
			for(int j=0; j<N_OUTPUT; j++){
				sum += w2[i][j] * deltas[j]; //誤差の逆伝播
			}

			double delta = hidden[i] * (1.0-hidden[i]) * sum;
			for(int j=0; j<N_INPUT; j++){
				w1[j][i] += alpha * delta * input[j];
			}
			b1[i] += alpha * delta;
		}
	}

	// 二乗誤差
	public double calcError(double teach[]){
		double e = 0.0;
		for(int i=0; i<teach.length; i++){
			e += Math.pow(teach[i]-output[i], 2.0);
		}
		e *= 0.5;
		return e;
	}

	// 学習
	public void learn( double[][] knownInputs, double[][] teach ) {

		int step = 0; //試行回数
		while ( true ) {

			double e = 0.0; // 二乗誤差の総和(初期値は0.0)

			// すべての訓練データをニューラルネットワークに入力・計算・誤差伝搬
			for(int i=0; i<knownInputs.length; i++){
				compute(knownInputs[i]);
				backPropagation(teach[i]);
				e += calcError(teach[i]);
			}

			// 100刻みで誤差を表示
			if ( step % 100 == 0) {
				System.out.println("step:" + step + ", loss=" + e);
			}

			// 二乗誤差が十分小さくなったら、終了
			if(e < 0.0001){
				break;
			}

			step++;
		}

	}

}






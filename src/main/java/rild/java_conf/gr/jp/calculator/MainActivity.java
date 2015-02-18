package rild.java_conf.gr.jp.calculator;
/*
reference
http://www.javadrive.jp/start/wrapper_class/index5.html
    文字列から数値への変換
http://codezine.jp/article/detail/5957?p=2#dl
    サンプルアプリ
http://mrbool.com/how-to-create-a-calculator-app-for-android/28100
    How to create a Calculator App for Android
http://stackoverflow.com/questions/5620772/get-text-from-pressed-button
    get text from pressed button
http://sholler.hatenablog.com/entry/2013/12/29/185549
    【Android】電卓を作る

 */


import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends ActionBarActivity {

    TextView textView;

    String numberStringA="0";
    String numberStringB="0";
    String answer = "";

    String setNumber="";

    int state=0;
    /*
    0 状態1）数値Aの入力中
    1 状態2）四則演算の選択（×÷＋－）
    2 状態3）数値Bの入力中
    3 状態4）演算した結果を表示中
    */
    String Ope="";
    /*
    Ope=
    (def)0,
    plu1,
    min2,
    tim3,
    div4
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.display);
        textView.setText(numberStringA);

    }
    public void onClickButton(View view){
        Button b = (Button)view;
        String instanceS = b.getText().toString();
//状態分岐
        //状態1）数値Aの入力中
            //数値入力でA入力待ち
            //演算子入力でB入力待ち
            //＝入力で結果表示
            //C入力でA入力待ち　
            // AC入力でA入力待ち
        if(state == 0){
//イベント分岐
            //初めはここにくる　＜＝　state=0と初期化してあるため
            // 数値
            if(chkEvent(instanceS) == 0){
                state = 0;//数値A入力受付続行
                setNumber += instanceS;
                //a += s ⇔ a = a + s
                textView.setText(setNumber);


            }
            // 演算子
            else if(chkEvent(instanceS) == 1){
                //状態2に遷移
                state = 1;
                //数値Aの確定
                numberStringA = setNumber;
                //演算子の確定
                Ope = instanceS;

                textView.setText(numberStringA);
            }
            // ＝
            else if(chkEvent(instanceS) == 2){
                state = 3;

                    textView.setText(numberStringA);

                }

            // C
            else if(chkEvent(instanceS) == 3){
                //Clear
                state = 0;
                numberStringA="0";
                setNumber="";
                textView.setText(numberStringA);

            }
            // AC
            else if(chkEvent(instanceS) == 4){
                state = 0;
                numberStringA="0";
                numberStringB="0";
                setNumber="";
                textView.setText(numberStringA);
            }

        }

        // 状態2）四則演算の選択（×÷＋－）
            //数値入力でB入力待ち
            //演算子入力でB入力待ち
            //＝入力で結果表示
            //C入力でA入力待ち　
            // AC入力でA入力待ち
        else if(state == 1){

            if(chkEvent(instanceS) == 0){
                state = 2;
                //状態３に遷移
                setNumber = instanceS;
                textView.setText(setNumber);

            }
            else if(chkEvent(instanceS) == 1){
                state = 1;
                //演算子入力状態の維持

                Ope = instanceS;
                //演算子の上書き

            }
            else if(chkEvent(instanceS) == 2){
                state = 3;
                //計算結果表示状態に遷移
                answer = calc(numberStringA,numberStringB,Ope);
                textView.setText(answer);

            }
            else if(chkEvent(instanceS) == 3){
                state = 0;
                numberStringA="0";
                setNumber="";
                textView.setText(numberStringA);
            }
            else if(chkEvent(instanceS) == 4){
                state = 0;
                numberStringA="0";
                numberStringB="0";
                setNumber="";
                textView.setText(numberStringA);
            }

        }

        // 状態3）数値Bの入力中
            //数値入力でB入力待ち
            //演算子入力でB入力待ち
            //＝入力で結果表示
            //C入力でB入力待ち　
            // AC入力でA入力待ち
        else if(state == 2){
            if(chkEvent(instanceS) == 0){
                state = 2;
                //数値B入力を続行
                setNumber += instanceS;
                //a += s ⇔ a = a + s
                textView.setText(setNumber);
            }
            else if(chkEvent(instanceS) == 1){
                state = 2;
                //計算の続行
                numberStringB = setNumber;
                //状態２→状態３の遷移の際に
                // setNumber = instanceSとなるため, 状態１の時の値は残らない
                answer = calc(numberStringA,numberStringB,Ope);
                textView.setText(answer);
                numberStringA = answer;
                //数値の引き継ぎ
                Ope = instanceS;
                //演算子の上書き

            }
            else if(chkEvent(instanceS) == 2){
                state = 3;
                numberStringB = setNumber;
                answer = calc(numberStringA,numberStringB,Ope);
                textView.setText(answer);
                numberStringA = answer;
            }
            else if(chkEvent(instanceS) == 3){
                state = 2;
                numberStringB = "0";
                setNumber = "";
                textView.setText(numberStringB);


            }
            else if(chkEvent(instanceS) == 4){
                state = 0;
                numberStringA="0";
                numberStringB="0";
                setNumber="";
                textView.setText(numberStringA);
            }


        }

        // 状態4）演算した結果を表示中
            //数値入力でA入力待ち
            //演算子入力で演算子入力状態；状態２（B入力待ち）
            //＝入力で結果表示
            //C　AC入力でA入力待ち
        else if(state == 3){
            if(chkEvent(instanceS) == 0){
                state = 0;
                //状態１に遷移
                setNumber = instanceS;
                textView.setText(setNumber);
                //入力した数字の表示
            }
            else if(chkEvent(instanceS) == 1){
                state = 1;
                Ope = instanceS;
                numberStringA = answer;
            }
            else if(chkEvent(instanceS) == 2){
                state = 3;
                answer = calc(numberStringA,numberStringB,Ope);
                textView.setText(answer);
                numberStringA = answer;
            }
            else if(chkEvent(instanceS) == 3){
                state = 0;
                numberStringA="0";
                numberStringB="0";
                setNumber="";
                textView.setText(numberStringA);
                //状態４におけるCはACと同じ効果
            }
            else if(chkEvent(instanceS) == 4){
                state = 0;
                numberStringA="0";
                numberStringB="0";
                setNumber="";
                textView.setText(numberStringA);
            }


        }
    }

/*
//ごり押しの残骸…
//後学のために残して置きます（消したい…）
    public void Plus(View v){
        translateA();

        Ope = 1;


      //numberA = plus(numberA,numberB);
       // textView.setText(String.valueOf(numberA));

    }
    public void Minus(View v){
        translateA();

        Ope = 2;

       // numberA = minus(numberA,numberB);
       // textView.setText(String.valueOf(numberA));

    }
    public void Times(View v){
        translateA();

        Ope = 3;
        numberA = times(numberA,numberB);
        textView.setText(String.valueOf(numberA));

    }
    public void Divide(View v){
        translateA();

        Ope = 4;


        numberA = divide(numberA,numberB);
        textView.setText(String.valueOf(numberA));

    }
    public void Equal(View v){
        if(Ope==0){

        }if(Ope==1){

        }if(Ope==2){

        }if(Ope==3){

        }if(Ope==4){

        }
    }


    public void One(View v){

        if(state ==0){
            numberStringA = numberStringA+ "1";

        }else{
            numberStringB = numberStringB+ "1";
        }

    }
    public void Two(View v){
        if(state ==0){
            numberStringB = numberStringA+ "2";

        }else{
            numberStringB = numberStringB+ "2";
        }

    }
    public void Tree(View v){
        if(state ==0){
            numberStringA = numberStringA+ "3";

        }else{
            numberStringB = numberStringB+ "3";
        }

    }
    public void Four(View v){
        if(state ==0){
            numberStringA = numberStringA+ "4";
        }else{
            numberStringB = numberStringB+ "4";
        }


    }
    public void Five(View v){
        if(state ==0){
            numberStringA = numberStringA+ "5";

        }else{
            numberStringB = numberStringB+ "5";
        }

    }
    public void Six(View v){
        if(state ==0){
            numberStringA = numberStringA+ "6";

        }else{
            numberStringB = numberStringB+ "6";
        }

    }
    public void Seven(View v){
        if(state ==0){
            numberStringA = numberStringA+ "7";

        }else{
            numberStringB = numberStringB+ "7";
        }

    }
    public void Eight(View v){
        if(state ==0){

            numberStringA = numberStringA+ "8";

        }else{
            numberStringB = numberStringB+ "8";
        }
    }
    public void Nine(View v){
        if(state ==0){
            numberStringA = numberStringA+ "9";

        }else{
            numberStringB = numberStringB+ "9";
        }

    }
    public void Zero(View v){
        if(state ==0){
            numberStringA = numberStringA+ "0";
        }else{
            numberStringB = numberStringB+ "0";
        }


    }


*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private int chkEvent(String s){
        if(s.equals("+") || s.equals("-") || s.equals("×") || s.equals("÷")||
                s.equals(" +/-")){ //条件分岐にString型を使う場合, ＝＝は使えない。
            return 1;
        }else if(s.equals("=")){
            return 2;
        }else if(s.equals("C")){
            return 3;
        }else if(s.equals("AC")){
            return 4;
        }else{
            return 0;
        }
        /*switch (s){
            case ("+"):
            case ("-"):
            case ("×"):
            case ("÷"):
                return 1;
               // break;

            case("="):
                    return 2;
               // break;

            case("C"):
                return 2;
           // break;

            case("AC"):
                return 2;
           // break;

            default:
                return 0;
        }*/
    }

public String calc(String a,String b,String o){
    double double_numberA = Double.parseDouble(a);
    double double_numberB = Double.parseDouble(b);
    double ans = 0;

    if(o.equals("+")){
        ans = plus(double_numberA,double_numberB);
    } if(o.equals("-")){
        ans = minus(double_numberA,double_numberB);
    } if(o.equals("×")){
        ans = times(double_numberA,double_numberB);
    } if(o.equals("÷")){
        ans = divide(double_numberA,double_numberB);
    }

    return Double.toString(ans);
}

    public double plus(double a,double b) {
        double c;
        c = a + b;
        return c;
    }

    public double minus(double a, double b) {
        double c;
        c = a - b;
        return c;
    }
    public double times(double a, double b) {
        double c;
        c = a * b;
        return c;
    }
    public double divide(double a, double b) {
        double c;
        c = a / b;
        return c;
    }


}


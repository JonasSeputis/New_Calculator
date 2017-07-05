package com.example.pc.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TEXT_VIEW_KEY = "text_view_key";

    Button _zeroB, _oneB, _twoB, _threeB, _fourB, _fiveB, _sixB, _sevenB, _eightB, _nineB, _dotB,
            _equalB, _plusB, _minusB, _divideB, _multiplyB, _bracketsB, _squareRootB,
            _deleteAllB, _backSpaceB;
    TextView _textView;
    HorizontalScrollView _scroll;   //I guess it's not the best approach, but couldn't thought anything else at the moment
    int _noOpenBracket = 0;

    MathEval ans = new MathEval();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setClickListeners();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        _textView.setText(savedInstanceState.getString(TEXT_VIEW_KEY));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(TEXT_VIEW_KEY, _textView.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void init() {
        _zeroB = (Button) findViewById(R.id.zero_button);
        _oneB = (Button) findViewById(R.id.one_button);
        _twoB = (Button) findViewById(R.id.two_button);
        _threeB = (Button) findViewById(R.id.three_button);
        _fourB = (Button) findViewById(R.id.four_button);
        _fiveB = (Button) findViewById(R.id.five_button);
        _sixB = (Button) findViewById(R.id.six_button);
        _sevenB = (Button) findViewById(R.id.seven_button);
        _eightB = (Button) findViewById(R.id.eight_button);
        _nineB = (Button) findViewById(R.id.nine_button);
        _dotB = (Button) findViewById(R.id.dot_button);
        _equalB = (Button) findViewById(R.id.equal_button);
        _plusB = (Button) findViewById(R.id.plus_button);
        _minusB = (Button) findViewById(R.id.minus_button);
        _divideB = (Button) findViewById(R.id.divide_button);
        _multiplyB = (Button) findViewById(R.id.multiply_button);
        _bracketsB = (Button) findViewById(R.id.brackets_button);
        _squareRootB = (Button) findViewById(R.id.square_root_button);
        _deleteAllB = (Button) findViewById(R.id.delete_all_button);
        _backSpaceB = (Button) findViewById(R.id.backspace_button);
        _textView = (TextView) findViewById(R.id.input_text_view);
        _scroll = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
    }

    private void setClickListeners() {
        _zeroB.setOnClickListener(this);
        _oneB.setOnClickListener(this);
        _twoB.setOnClickListener(this);
        _threeB.setOnClickListener(this);
        _fourB.setOnClickListener(this);
        _fiveB.setOnClickListener(this);
        _sixB.setOnClickListener(this);
        _sevenB.setOnClickListener(this);
        _eightB.setOnClickListener(this);
        _nineB.setOnClickListener(this);
        _bracketsB.setOnClickListener(this);
        _dotB.setOnClickListener(this);
        _plusB.setOnClickListener(this);
        _minusB.setOnClickListener(this);
        _divideB.setOnClickListener(this);
        _multiplyB.setOnClickListener(this);
        _squareRootB.setOnClickListener(this);
        _deleteAllB.setOnClickListener(this);
        _backSpaceB.setOnClickListener(this);
        _equalB.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String textFromTextView = _textView.getText().toString();
        String typeOfTheLastChar = findTheTypeOfTheLastChar(textFromTextView);

        if(textFromTextView.contains(getString(R.string.equal))){
            _textView.setText("");
        }
        switch (view.getId()) {
            case R.id.zero_button:
                addNumberToTextView(typeOfTheLastChar, 0);
                break;
            case R.id.one_button:
                addNumberToTextView(typeOfTheLastChar, 1);
                break;
            case R.id.two_button:
                addNumberToTextView(typeOfTheLastChar, 2);
                break;
            case R.id.three_button:
                addNumberToTextView(typeOfTheLastChar, 3);
                break;
            case R.id.four_button:
                addNumberToTextView(typeOfTheLastChar, 4);
                break;
            case R.id.five_button:
                addNumberToTextView(typeOfTheLastChar, 5);
                break;
            case R.id.six_button:
                addNumberToTextView(typeOfTheLastChar, 6);
                break;
            case R.id.seven_button:
                addNumberToTextView(typeOfTheLastChar, 7);
                break;
            case R.id.eight_button:
                addNumberToTextView(typeOfTheLastChar, 8);
                break;
            case R.id.nine_button:
                addNumberToTextView(typeOfTheLastChar, 9);
                break;
            case R.id.dot_button:
                if (typeOfTheLastChar.equals(getString(R.string.num_type)) ||
                        typeOfTheLastChar.equals(getString(R.string.closed_bracket_type)))
                    _textView.append(getString(R.string.dot));
                break;
            case R.id.plus_button:
                if (typeOfTheLastChar.equals(getString(R.string.num_type)) ||
                        typeOfTheLastChar.equals(getString(R.string.closed_bracket_type)))
                    _textView.append(getString(R.string.plus));
                break;
            case R.id.minus_button:
                if (typeOfTheLastChar.equals(getString(R.string.num_type)) ||
                        typeOfTheLastChar.equals(getString(R.string.closed_bracket_type)))
                    _textView.append(getString(R.string.subtraction));
                break;
            case R.id.multiply_button:
                if (typeOfTheLastChar.equals(getString(R.string.num_type)) ||
                        typeOfTheLastChar.equals(getString(R.string.closed_bracket_type))
                        && _noOpenBracket == 0)
                    _textView.append(getString(R.string.multiplication));
                break;
            case R.id.divide_button:
                if (typeOfTheLastChar.equals(getString(R.string.num_type)) ||
                        typeOfTheLastChar.equals(getString(R.string.closed_bracket_type))
                        && _noOpenBracket == 0)
                    _textView.append(getString(R.string.division));
                break;
            case R.id.brackets_button:
                _textView.append(addBrackets(typeOfTheLastChar));
                break;
            case R.id.backspace_button:
                deleteLastValue();
                break;
            case R.id.delete_all_button:
                _textView.setText("");
                break;
            case R.id.square_root_button:
                _textView.append(addRoot(typeOfTheLastChar));
                break;
            case R.id.equal_button:
                if(!textFromTextView.contains(getString(R.string.equal)) ||
                        !textFromTextView.isEmpty()) {
                    _textView.append(getString(R.string.equal) + ans.evaluate(textFromTextView));
                    _scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                }
                break;
        }
    }

    private String findTheTypeOfTheLastChar(String text) {
        if (text.length() == 0) {
            return getString(R.string.none_type);
        } else {
            char lastChar = text.charAt(text.length() - 1);
            String type;

            switch (lastChar) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    type = getString(R.string.num_type);
                    break;
                case '+':
                case '-':
                    type = getString(R.string.plus_minus_type);
                    break;
                case '.':
                    type = getString(R.string.dot_type);
                    break;
                case '*':
                case '/':
                    type = getString(R.string.multi_div_type);
                    break;
                case '(':
                    type = getString(R.string.open_bracket_type);
                    break;
                case ')':
                    type = getString(R.string.closed_bracket_type);
                    break;
                default:
                    type = getString(R.string.none_type);
            }
            return type;
        }
    }

    public void addNumberToTextView(String type, int number) {
        if (type.equals(getString(R.string.closed_bracket_type))) {
            _textView.append(getString(R.string.multiplication) + getString(R.string.open_bracket)
                    + number);
            _noOpenBracket++;
        } else {
            _textView.append(String.valueOf(number));
        }
                _scroll.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
    }

    public String addBrackets(String typeOfTheLastChar) {
        switch (typeOfTheLastChar) {
            case "OPEN_BRACKET":
                _noOpenBracket++;
                return "(";
            case "NUM":
            case "CLOSED_BRACKET":
                if (_noOpenBracket > 0) {
                    _noOpenBracket--;
                    return ")";
                } else {
                    _noOpenBracket++;
                    return "*(";
                }
            case "PLUS_MINUS":
                _noOpenBracket++;
                return "(";
            case "NONE":
            case "MULTI_DIV":
                _noOpenBracket++;
                return "(";
            default:
                return null;
        }
    }

    public String addRoot(String typeOfTheLastChar) {
        switch (typeOfTheLastChar) {
            case "OPEN_BRACKET":
            case "PLUS_MINUS":
            case "MULTI_DIV":
            case "NONE":
                return getString(R.string.square_root_symbol);
            case "NUM":
            case "CLOSED_BRACKET":
                return getString(R.string.multiplication) + getString(R.string.square_root_symbol);
            default:
                return null;
        }
    }

    private void deleteLastValue() {
        if (_textView.getText().toString().length() > 0) {
            if (_textView.getText().toString().substring(
                    _textView.getText().toString().length() - 1).equals(" ")) {
                _textView.setText(_textView.getText().toString()
                        .substring(0, _textView.getText().toString().length() - 2));
            } else {
                _textView.setText(_textView.getText().toString()
                        .substring(0, _textView.getText().toString().length() - 1));
            }
        }
    }
}
package com.example.myapplication
import android.util.Log
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentFirstBinding
import kotlin.math.ceil
import kotlin.math.floor


class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var ind = 0
    private val operands = listOf('+', '-', '/', '*')
    private val calcString = mutableListOf<Char>('|')
    private val operatives = listOf('(', ')', '+', '-', '/', '*')
    private var numOpens = 0
    private var numClosed = 0


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.button0.setOnClickListener{
            zeroKey()
            display()
        }
        binding.button1.setOnClickListener{
            oneKey()
            display()
        }
        binding.button2.setOnClickListener{
            twoKey()
            display()
        }
        binding.button3.setOnClickListener{
            threeKey()
            display()
        }
        binding.button4.setOnClickListener{
            fourKey()
            display()
        }
        binding.button5.setOnClickListener{
            fiveKey()
            display()
        }
        binding.button6.setOnClickListener{
            sixKey()
            display()
        }
        binding.button7.setOnClickListener{
            sevenKey()
            display()
        }
        binding.button8.setOnClickListener{
            eightKey()
            display()
        }
        binding.button9.setOnClickListener{
            nineKey()
            display()
        }
        binding.buttonOpen.setOnClickListener{
            openKey()
            display()
        }
        binding.buttonClose.setOnClickListener{
            closeKey()
            display()
        }
        binding.buttonLeft.setOnClickListener{
            leftKey()
            display()
        }
        binding.buttonRight.setOnClickListener{
            rightKey()
            display()
        }
        binding.buttonEq.setOnClickListener{
            calculate()
        }
        binding.buttonAdd.setOnClickListener{
            addKey()
            display()
        }
        binding.buttonSub.setOnClickListener{
            subKey()
            display()
        }
        binding.buttonMul.setOnClickListener{
            mulKey()
            display()
        }
        binding.buttonDiv.setOnClickListener{
            divKey()
            display()
        }
        binding.buttonDelete.setOnClickListener{
            deleteKey()
            display()
        }
        binding.buttonClear.setOnClickListener{
            clearKey()
            display()
        }
        binding.buttonDecimal.setOnClickListener{
            decimalKey()
            display()
        }
        display()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun display(){
        var equation :String = ""
        for (x in calcString){
            equation += x
        }
        binding.textViewEquation.text = equation
    }

    private fun calculate() {
        var ignored = false
        if (calcString.last() == '|'){
            calcString.removeLast()
            ignored = true
        }
        binding.textviewResult.text = if (numOpens != numClosed) {
            "Please match parentheses"
        } else if (calcString.last() in operands) {
            "Please complete equation"
        } else if (group() == "undefined") {
            "Undefined, please do not divide by 0"
        } else {
            val num = group().toFloat()
            val result : String = if (ceil(num) == floor(num)){
                num.toInt().toString()
            } else {
                num.toString()
            }
            "= $result"
        }
        if(ignored){
            calcString.add('|')
        }
    }

    private fun group() : String {
        val contents = mutableListOf<String>()
        var word : String = ""
        for (x in calcString) {
            when (x) {
                '|' -> {
                }
                in operatives -> {
                    if (word.isNotEmpty()) {
                        contents.add(word)
                        word = ""
                    }
                    contents.add(x.toString())
                }
                else -> {
                    word += x
                }
            }
        }
        if (word.isNotEmpty()) {
            contents.add(word.toString())
        }
        return parse(contents)
    }

    private fun parse(inString : MutableList<String>) :String {
        var contents = mutableListOf<String>()
        for (x in inString){
            contents.add(x)
        }
        var x = 0
        while (x < contents.size) {
            val temp = contents[x]
            if (temp == "(") {
                var depth = 1
                val inner = mutableListOf<String>()
                var j = x + 1
                while (depth > 0) {
                    inner.add(contents[j])
                    when (contents[j]) {
                        "(" -> depth++
                        ")" -> {
                            depth--
                            if (depth == 0) {
                                inner.removeLast()
                            }
                        }
                    }
                    j++
//                    if (j >= contents.size){
//                        return ""
//                    }
                }
                val innerResults = parse(inner)
                if (innerResults == "undefined"){
                    return "undefined"
                }
                val newContents = mutableListOf<String>()
                for (k in 0..<x) {
                    newContents.add(contents[k])
                }
                newContents.add(innerResults)
                if (j < contents.size) {
                    for (k in j..<contents.size) {
                        newContents.add(contents[k])
                    }
                }
                contents = newContents
            }
            x++
        }
        x = 0
        while (x < (contents.size)) {
            val temp = contents[x]
            if (temp in arrayOf("*","/")) {
                var value : Float = 0F
                if ((temp == "/") and(contents[x+1].toFloat() == 0f)){
                    return "undefined"
                }
                when (temp){
                    "*" -> value = contents[x-1].toFloat() * contents[x+1].toFloat()
                    "/" -> value = contents[x-1].toFloat() / contents[x+1].toFloat()
                }
                val newContents = mutableListOf<String>()
                for (k in 0..(x-2)) {
                    newContents.add(contents[k])
                }
                newContents.add(value.toString())
                if ((x+2 < contents.size)){
                    for (k in (x+2)..<contents.size) {
                        newContents.add(contents[k])
                    }
                }
                contents = newContents
                x--
            }
            x++
        }
        x = 0
        while (x < (contents.size)) {
            val temp = contents[x]
            if (temp in arrayOf("+","-")) {
                var value : Float = 0F
                when (temp){
                    "+" -> value = contents[x-1].toFloat() + contents[x+1].toFloat()
                    "-" -> value = contents[x-1].toFloat() - contents[x+1].toFloat()
                }
                val newContents = mutableListOf<String>()
                for (k in 0..(x-2)) {
                    newContents.add(contents[k])
                }
                newContents.add(value.toString())
                if ((x+2 < contents.size)){
                    for (k in (x+2)..<contents.size) {
                        newContents.add(contents[k])
                    }
                }
                contents = newContents
                x--
            }
            x++
        }
        return contents[0]
    }


    private fun keyCheck(key : Char) : Boolean{
        if(ind > 0) {
            if (calcString[ind - 1] == key) {
                return true
            }
        }
        return false
    }

    private fun oneKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '1')
        ind++
    }

    private fun twoKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '2')
        ind++
    }

    private fun threeKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind ,'3')
        ind++
    }

    private fun fourKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '4')
        ind++
    }

    private fun fiveKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '5')
        ind++
    }

    private fun sixKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '6')
        ind++
    }

    private fun sevenKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '7')
        ind++
    }

    private fun eightKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '8')
        ind++
    }

    private fun nineKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '9')
        ind++
    }

    private fun zeroKey(){
        if(keyCheck(')')) mulKey()
        calcString.add(ind, '0')
        ind++
    }

    private fun decimalKey(){
        if(ind == 0) {
            zeroKey()
        } else if (calcString[ind-1] in operatives) {
            zeroKey()
        } else {
            var nDone = true
            var i = ind
            while (nDone){
                i--
                if (i < 0){
                    nDone = false
                } else if (calcString[i] in operatives){
                    nDone = false
                } else if (calcString[i] == '.'){
                    return
                }
            }
        }
        calcString.add(ind, '.')
        ind++
    }

    private fun addKey(){
        if (ind == 0){
            return
        } else if (calcString[ind-1] in operands){
            calcString[ind-1] = '+'
        } else if (!keyCheck('(')){
            calcString.add(ind, '+')
            ind++
        }
    }

    private fun subKey(){
        if (ind == 0){
            return
        } else if (calcString[ind-1] in operands){
            calcString[ind-1] = '-'
        } else if (!keyCheck('(')){
            calcString.add(ind, '-')
            ind++
        }
    }

    private fun mulKey(){
        if (ind == 0){
            return
        } else if (calcString[ind-1] in operands){
            calcString[ind-1] = '*'
        } else if (!keyCheck('(')){
            calcString.add(ind, '*')
            ind++
        }
    }

    private fun divKey(){
        if (ind == 0){
            return
        } else if (calcString[ind-1] in operands) {
            calcString[ind - 1] = '/'
        } else if (!keyCheck('(')){
            calcString.add(ind, '/')
            ind++
        }
    }

    private fun openKey(){
        if (ind > 0){
            if (keyCheck('.')) {
                calcString[ind - 1] = '*'
            } else if (calcString[ind-1] in listOf('0','1','2','3','4','5','6','7','8','9','0')){
                calcString.add(ind, '*')
                ind++
            }
        }
        calcString.add(ind, '(')
        numOpens++
        ind++
    }

    private fun closeKey(){
        if (keyCheck('(')){
            calcString.removeAt(ind-1)
            numOpens--
            ind--
        } else if (numOpens > numClosed) {
            calcString.add(ind, ')')
            numClosed++
            ind++
        }
    }

    private fun leftKey(){
        if(ind > 0) {
            calcString.remove('|')
            ind--
            calcString.add(ind, '|')
        }
    }

    private fun rightKey() {
        if (ind < (calcString.size-1)) {
            calcString.remove('|')
            ind++
            calcString.add(ind, '|')
        }
    }

    private fun deleteKey(){
        if(ind > 0) {
            if (calcString[ind-1] == '(') {
                if (numOpens == numClosed) {
                    return
                } else{
                    numOpens--
                }
            } else if (calcString[ind-1] == ')'){
                numClosed--
            }
            ind--
            calcString.removeAt(ind)
        }
    }

    private fun clearKey(){
        calcString.clear()
        calcString.add('|')
        ind = 0
        binding.textviewResult.text = ""
    }

}
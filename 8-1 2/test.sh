echo $1
java Compiler < $1 > ${1/jack/vm}
diff ${1/jack/vm} ${1/jack/vm.cmp}
if [ $? -eq 0 ]
then
    echo "テストは成功しました"
else
    echo "テストは失敗しました"
fi
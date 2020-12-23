java Tokenizer < Main.jack > token.out
diff token.out token.cmp
if [ $? -eq 0 ]
then
    echo "テストは成功しました"
else
    echo "テストは失敗しました"
fi

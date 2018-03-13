#!/bin/bash
typeset -i id=0
( printf "[\n"
for filename in `find Datas -name "*.penta"`
do
id=id+1
name=`basename $filename .penta | sed -e "s/_/ /g"`
dir=`dirname $filename`
diffi=`basename $dir`

printf "{\n"
cat $filename | sed -e "1s/^\(.*\)$/\"author\":\"\1\",\"name\":\"$name\",/" -e "2s/\([0-9][0-9]*\) \([0-9][0-9]*\)/\"width\":\2,\"height\":\1,/" |head -2

typeset -i difficulty
difficulty=`echo $name | sed "s/^\(.\).*$/\1/"`
difficulty=$diffi
if ((difficulty == 0)) || ((difficulty > 5))
then
#default value is 1 
difficulty=1
fi
echo "\"difficulty\":$difficulty,"

echo "\"data\":["
cat $filename | tail +3 | grep "^[0-9A-z][0-9A-z]*$" | sed -e "/^[0-9A-Za-z][0-9A-Za-z]*$/s/\([A-Za-z0-9]\)/\"\1\",/g" -e "s/,$//" -e 's/^/\[/' -e "s/$/],/"
echo "],"

#Valeurs....
echo "\"values\":["
cat $filename | grep "^[0-9],[0-9][0-9]*,[0-9][0-9]*$" | sed -e "s/\([0-9]\),\([0-9][0-9]*\),\([0-9][0-9]*\)/{ \"val\":\1, \"i\":\2, \"j\":\3 },/"
echo "],"

#Identities ....
echo "\"sisters\": ["
typeset -i o
o=0
for i in `cat $filename | grep "^[^0-9],[0-9][0-9]*,[0-9][0-9]*$"| sed -e "s/^\(.\).*$/XXXX\1XXXX/" | sort -u `
	do
	o=o+1

	TEMP=/tmp/temp.$$.txt
	rm -f $TEMP
	#Arnaque au shell pour eviter les problemes de completion de fichier
        cat $filename | sed -e "s/^\(.\)/XXXX\1XXXX/" > $TEMP

        echo "{ \"id\":$o, \"symbol\":\".\", \"positions\":[ "
	grep --fixed-strings "${i}," $TEMP | sed -e "s/^XXXX.XXXX,\([0-9][0-9]*\),\([0-9][0-9]*\)$/{\"i\":\1,\"j\":\2},/"
	rm -f $TEMP

	echo "] },"
done
echo "],"

#Differences
echo "\"differences\": ["
cat $filename | grep "^-[0-9][0-9]*,[0-9][0-9]*,[0-9][0-9]*,[0-9][0-9]*$" | sed -e "s/-\([0-9][0-9]*\),\([0-9][0-9]*\),\([0-9][0-9]*\),\([0-9][0-9]*\)/{\"position1\":{\"i\":\1,\"j\":\2},\"position2\":{\"i\":\3,\"j\":\4}},/"
echo "]},"

done
printf "]\n" ) > Datas/all.json

#!/bin/sh
rm -f index.html
INDEX=`find . \( ! -regex '.*/\..*' \) -type f -exec echo "<a href=\"{}\">{}</a><br/>" ";"`
echo "<html><head><title>index</title></head><body>$INDEX</body></html>" > index.html

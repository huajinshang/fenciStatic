//封装字典树

function Tire() {

    this.root = new Node(null);
}

Tire.prototype = {
    //初始化树
    initTire(dict) {
        for (var i = 0; i < dict.length; i++) {
            var code = dict[i];
            this.addTire(code);
        }
    },

    //去除左空格
    removeLeftSpace(str) {
        return str.replace(/^\s*/, '');
    },

//添加词汇放入树中
    addTire(e) {
        var node = this.root;
        //此处在添加之前进行除空格操作
        var e=removeLeftSpace(e);
        for (var i = 0; i < e.length; i++) {
            var c = e[i];
            if (!(c in node.childs)) {
                node.childs[c] = new Node(c);
            }
            node = node.childs[c];
        }
        node.asWord(); // 成词边界
    }
    ,

//截取所想排查的词汇
    splitWords(words) {
        var start = 0, end = words.length, result = [];
        while (start != end) {
            var word = [];
            for (var i = start; i < end; i++) {
                if (end == 1) {
                    var b = words;
                } else {
                    var b = words[i];
                }
                word.push(b);

                var finds = this.search(word);
                if (finds !== false && finds.length > 0) {
                    // 如果在字典中，则添加到分词结果集
                    result = result.concat(finds);
                }
            }
            start++;
        }
        return result;
    }
    ,

//查找
    search(word) {
        var node = this.root, result = [], bet = [];
        for (var i = 0; i < word.length; i++) {
            var childs = node.childs;
            if (!(word[i] in childs)) {
                return result;
            } else {
                bet.push(word[i]);
            }

            // 如果是停止词，则退出
            if (bet.join('') in stop) {
                return result;
            }

            var code = childs[word[i]];
            if (code.isWord()) {
                code.addCount(); // 用于计数判断
                result.push(bet.join(''));
            }
            node = code;
        }
        return result;
    }

}//结尾


// 节点
function Node(_byte) {
    this.childs = {}; // 子节点集合
    this._byte = _byte || null; // 此节点上存储的字节
    this._isWord = false; // 边界保存，表示是否可以组成一个词
    this._count = 0;
}

Node.prototype = {
    isWord: function () {
        return (this._isWord && (this._count == 0));
    },
    asWord: function () {
        this._isWord = true;
    },
    addCount: function () {
        this._count++;
    },
    getCount: function () {
        return this._count;
    }
};

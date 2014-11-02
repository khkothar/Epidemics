function [WP, DP] = LDADecomposition(r)
    fileName = './Data/X.csv';
    X = csvread(fileName);
    [~,wordcount] = size(X);
    [i, j, k] = find(sparse(X));
    wd = sparse(j,i,k);
    [ WS , DS ] = SparseMatrixtoCounts(wd);
    [ WP,DP,~ ] = GibbsSamplerLDA(WS, DS, r, 300, 50/r, 200/wordcount, 3, 1);
    dpFileName = './Data/DP.csv';
    WP = full(WP);
    DP = full(DP);
    csvwrite(dpFileName, DP);
end
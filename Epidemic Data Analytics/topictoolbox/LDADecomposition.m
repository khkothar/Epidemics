function [WP, DP] = LDADecomposition(r, isQuery)
	fileName = './Data/LDAInputMatrix.csv';
	dpFileName = './Data/LDAOutFile.csv';
	%if(isQuery)
		%fileName = './Data/QUERY.csv';
		%dpFileName = './Data/LDAQueryOutFile.csv';
	%else			
	%end
    X = csvread(fileName);
    [~,wordcount] = size(X);
    [i, j, k] = find(sparse(X));
    wd = sparse(j,i,k);
    [WS , DS] = SparseMatrixtoCounts(wd);
    [WP,DP,~] = GibbsSamplerLDA(WS, DS, r, 300, 50/r, 200/wordcount, 3, 1);
    WP = full(WP);
    DP = full(DP);
    [lastrow,~] = size(DP);
    if(isQuery)
    G2 = DP(lastrow,:);
        for n = 1:lastrow-1
            G1 = DP(n,:);
            D(n,1) = pdist2(G2,G1);
        end
        lastrow = --lastrow;
        csvwrite('./Data/LDAQueryLastRow.csv',DP(lastrow,:));
        csvwrite('./Data/LDAQuerySimilarity.csv', D);
        display(D);
    end
    csvwrite(dpFileName, DP(1:lastrow,:));    
end
function [U, S, V] = svDecomposition(r, isQuery)
    fileName = './Data/X.csv';
    X = csvread(fileName);
    [U, S, V] = svd(X);
    %Using only 'r' Semantics
    [m,n] = size(X);
    if(r<m && r<n)
        csvwrite('./Data/U.csv',U(:,1:r));
        csvwrite('./Data/S.csv',S(1:r,1:r));
        csvwrite('./Data/V.csv',V(1:r,:));
    end
    display(U(:, 1:r));
    [value,index] = sort(U(:, 1:r),1, 'descend');
    display(value);
    display(index);    
    
    if(isQuery)
        queryMatrix = csvread('./Data/QUERY.csv');
        query = U(:,1:r) * S(1:r,1:r) * V(1:r,:) * queryMatrix(:,1);
        csvwrite('./Data/SimilarResults.csv', query);
        end
end
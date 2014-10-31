function bandLength = getGaussianBands(r)
bandLength = zeros(1,r);
for i = 1:r
bandLength(i) = (integral(@(x) normpdf(x, 0, 0.25), (i-1)/r, i/r))/(integral(@(x) normpdf(x, 0, 0.25), 0, 1));
end
const fs = require("fs");

function decodeValue(value, base) {
  return parseInt(value, base);
}

function lagrangeInterpolation(points, xValue) {
  let result = 0;

  for (let i = 0; i < points.length; i++) {
    let term = points[i].y;
    for (let j = 0; j < points.length; j++) {
      if (i !== j) {
        term *= (xValue - points[j].x) / (points[i].x - points[j].x);
      }
    }
    result += term;
  }

  return Math.round(result);
}
function findConstantTerm(inputFile) {
  const input = JSON.parse(fs.readFileSync(inputFile, "utf8"));

  const n = input.keys.n;
  const k = input.keys.k;
  if (n < k) {
    throw new Error("Number of roots provided is less than required roots (k).");
  }
  const points = [];
  for (const key in input) {
    if (key === "keys") continue;

    const x = parseInt(key);
    const base = parseInt(input[key].base);
    const y = decodeValue(input[key].value, base);

    points.push({ x, y });
  }
  const selectedPoints = points.slice(0, k);
  const constantTerm = lagrangeInterpolation(selectedPoints, 0);

  return constantTerm;
}
try {
  const testCase1 = findConstantTerm("testcase1.json");
  const testCase2 = findConstantTerm("testcase2.json");

  console.log("Constant term for Test Case 1:", testCase1);
  console.log("Constant term for Test Case 2:", testCase2);

} catch (error) {
  console.error("Error:", error.message);
}

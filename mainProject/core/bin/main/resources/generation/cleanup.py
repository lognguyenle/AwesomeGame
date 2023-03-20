import pandas as pd
# data: https://github.com/plotly/datasets/blob/master/us-cities-top-1k.csv
df = pd.read_csv('./cities.csv')
df = df.sort_values('State')
df.to_csv('data.csv', index=False, header=False)
print(df['City'][0])
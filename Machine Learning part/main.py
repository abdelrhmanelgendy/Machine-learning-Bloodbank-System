import numpy as np
dataset = np.loadtxt('D:\\transfusion.data.csv',delimiter=',',skiprows=0)



np.set_printoptions(formatter={'float': '{:0.2f}'.format})


x = dataset[:,:-1]

y = dataset[:,-1]


mean = x.mean(axis=0)
x -= mean
std = x.std(axis=0)
x /= std

import tensorflow as tf
from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense

model = Sequential()
model.add(Dense(8,input_dim = len(x[0, : ]),activation='relu'))
model.add(Dense(4,activation='relu'))
model.add(Dense(1,activation='sigmoid'))

model.summary()

model.compile(loss='binary_crossentropy',optimizer='rmsprop',metrics=['accuracy'])

model.fit(x=x,y=y,epochs=2000,verbose=1)

model.predict(x)
prediction = model.predict(x)
a = np.array([[5,6,4,7]])
print(model.predict(a))
print(prediction[:5])



from flask import Flask
from flask import request

app = Flask(__name__)


@app.route('/predict',methods=['POST'])
def hello_predict():
    v1 = float(request.form["v1"])
    v2 = float(request.form["v2"])
    v3 = float(request.form["v3"])
    v4 = float(request.form["v4"])
    vAll = np.array([[v1,v2,v3,v4]])

    print(model.predict(vAll))
    pre =float( model.predict(vAll))
    print(str(pre))
    return str(pre)
     


if __name__ == "__main__":
    app.run(debug=True,host="0.0.0.0")


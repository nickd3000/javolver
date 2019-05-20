package com.physmo.javolver.animals;

public class SimpleAnimalEntity implements Entity {
    Vec2 position = new Vec2();
    double health = 1.0;
    double hungar = 1.0;
    double size = 1.0;

    @Override
    public void tick(double t) {

    }

    @Override
    public Vec2 getPosition() {
        return position;
    }

    public void initFromDna() {
        // are we going to roll our own neural network here or use pre existing one?
    }

}

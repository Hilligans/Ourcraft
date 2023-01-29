package dev.hilligans.ourcraft.Util.Math;

import org.joml.*;
import org.joml.Math;
import org.joml.Runtime;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Vector3fi implements Vector3fc {

    public final float x;
    public final float y;
    public final float z;

    public Vector3fi() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3fi(float d) {
        this.x = d;
        this.y = d;
        this.z = d;
    }

    public Vector3fi(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3fi(Vector3fc v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }

    public Vector3fi(Vector3ic v) {
        this.x = v.x();
        this.y = v.y();
        this.z = v.z();
    }

    public Vector3fi(Vector2fc v, float z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }

    public Vector3fi(Vector2ic v, float z) {
        this.x = v.x();
        this.y = v.y();
        this.z = z;
    }

    public Vector3fi(float[] xyz) {
        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }

    @Override
    public float x() {
        return x;
    }

    @Override
    public float y() {
        return y;
    }

    @Override
    public float z() {
        return z;
    }

    @Override
    public FloatBuffer get(FloatBuffer buffer) {
        return null;
    }

    @Override
    public FloatBuffer get(int index, FloatBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer get(ByteBuffer buffer) {
        return null;
    }

    @Override
    public ByteBuffer get(int index, ByteBuffer buffer) {
        return null;
    }

    @Override
    public Vector3fc getToAddress(long address) {
        return null;
    }

    public Vector3f sub(Vector3fc v, Vector3f dest) {
        dest.x = x - v.x();
        dest.y = y - v.y();
        dest.z = z - v.z();
        return dest;
    }

    public Vector3f sub(float x, float y, float z, Vector3f dest) {
        dest.x = this.x - x;
        dest.y = this.y - y;
        dest.z = this.z - z;
        return dest;
    }

    public Vector3f add(Vector3fc v, Vector3f dest) {
        dest.x = this.x + v.x();
        dest.y = this.y + v.y();
        dest.z = this.z + v.z();
        return dest;
    }

    public Vector3f add(float x, float y, float z, Vector3f dest) {
        dest.x = this.x + x;
        dest.y = this.y + y;
        dest.z = this.z + z;
        return dest;
    }

    public Vector3f fma(Vector3fc a, Vector3fc b, Vector3f dest) {
        dest.x = Math.fma(a.x(), b.x(), x);
        dest.y = Math.fma(a.y(), b.y(), y);
        dest.z = Math.fma(a.z(), b.z(), z);
        return dest;
    }

    public Vector3f fma(float a, Vector3fc b, Vector3f dest) {
        dest.x = Math.fma(a, b.x(), x);
        dest.y = Math.fma(a, b.y(), y);
        dest.z = Math.fma(a, b.z(), z);
        return dest;
    }

    public Vector3f mulAdd(Vector3fc a, Vector3fc b, Vector3f dest) {
        dest.x = Math.fma(x, a.x(), b.x());
        dest.y = Math.fma(y, a.y(), b.y());
        dest.z = Math.fma(z, a.z(), b.z());
        return dest;
    }

    public Vector3f mulAdd(float a, Vector3fc b, Vector3f dest) {
        dest.x = Math.fma(x, a, b.x());
        dest.y = Math.fma(y, a, b.y());
        dest.z = Math.fma(z, a, b.z());
        return dest;
    }

    public Vector3f mul(Vector3fc v, Vector3f dest) {
        dest.x = x * v.x();
        dest.y = y * v.y();
        dest.z = z * v.z();
        return dest;
    }

    public Vector3f div(Vector3fc v, Vector3f dest) {
        dest.x = x / v.x();
        dest.y = y / v.y();
        dest.z = z / v.z();
        return dest;
    }

    public Vector3f mulProject(Matrix4fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33())));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30()))) * invW;
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31()))) * invW;
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32()))) * invW;
        return dest;
    }

    public Vector3f mulProject(Matrix4fc mat, float w, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        float invW = 1.0f / Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33() * w)));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30() * w))) * invW;
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31() * w))) * invW;
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32() * w))) * invW;
        return dest;
    }

    public Vector3f mul(Matrix3fc mat, Vector3f dest) {
        float lx = x, ly = y, lz = z;
        dest.x = Math.fma(mat.m00(), lx, Math.fma(mat.m10(), ly, mat.m20() * lz));
        dest.y = Math.fma(mat.m01(), lx, Math.fma(mat.m11(), ly, mat.m21() * lz));
        dest.z = Math.fma(mat.m02(), lx, Math.fma(mat.m12(), ly, mat.m22() * lz));
        return dest;
    }

    public Vector3f mul(Matrix3dc mat, Vector3f dest) {
        float lx = x, ly = y, lz = z;
        dest.x = (float) Math.fma(mat.m00(), lx, Math.fma(mat.m10(), ly, mat.m20() * lz));
        dest.y = (float) Math.fma(mat.m01(), lx, Math.fma(mat.m11(), ly, mat.m21() * lz));
        dest.z = (float) Math.fma(mat.m02(), lx, Math.fma(mat.m12(), ly, mat.m22() * lz));
        return dest;
    }

    public Vector3f mul(Matrix3x2fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = z;
        return dest;
    }

    public Vector3f mulTranspose(Matrix3fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        return dest;
    }

    public Vector3f mulPosition(Matrix4fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return dest;
    }

    public Vector3f mulPosition(Matrix4x3fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return dest;
    }

    public Vector3f mulTransposePosition(Matrix4fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, Math.fma(mat.m02(), z, mat.m03())));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, Math.fma(mat.m12(), z, mat.m13())));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, Math.fma(mat.m22(), z, mat.m23())));
        return dest;
    }

    public float mulPositionW(Matrix4fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        float w = Math.fma(mat.m03(), x, Math.fma(mat.m13(), y, Math.fma(mat.m23(), z, mat.m33())));
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, Math.fma(mat.m20(), z, mat.m30())));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, Math.fma(mat.m21(), z, mat.m31())));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, Math.fma(mat.m22(), z, mat.m32())));
        return w;
    }


    public Vector3f mulDirection(Matrix4dc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = (float) Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = (float) Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = (float) Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return dest;
    }

    public Vector3f mulDirection(Matrix4fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return dest;
    }

    public Vector3f mulDirection(Matrix4x3fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m10(), y, mat.m20() * z));
        dest.y = Math.fma(mat.m01(), x, Math.fma(mat.m11(), y, mat.m21() * z));
        dest.z = Math.fma(mat.m02(), x, Math.fma(mat.m12(), y, mat.m22() * z));
        return dest;
    }

    public Vector3f mulTransposeDirection(Matrix4fc mat, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = Math.fma(mat.m00(), x, Math.fma(mat.m01(), y, mat.m02() * z));
        dest.y = Math.fma(mat.m10(), x, Math.fma(mat.m11(), y, mat.m12() * z));
        dest.z = Math.fma(mat.m20(), x, Math.fma(mat.m21(), y, mat.m22() * z));
        return dest;
    }

    public Vector3f mul(float scalar, Vector3f dest) {
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }

    public Vector3f mul(float x, float y, float z, Vector3f dest) {
        dest.x = this.x * x;
        dest.y = this.y * y;
        dest.z = this.z * z;
        return dest;
    }

    public Vector3f div(float scalar, Vector3f dest) {
        float inv = 1.0f / scalar;
        dest.x = this.x * inv;
        dest.y = this.y * inv;
        dest.z = this.z * inv;
        return dest;
    }

    public Vector3f div(float x, float y, float z, Vector3f dest) {
        dest.x = this.x / x;
        dest.y = this.y / y;
        dest.z = this.z / z;
        return dest;
    }

    public Vector3f rotate(Quaternionfc quat, Vector3f dest) {
        return quat.transform(this, dest);
    }

    public Quaternionf rotationTo(Vector3fc toDir, Quaternionf dest) {
        return dest.rotationTo(this, toDir);
    }

    public Quaternionf rotationTo(float toDirX, float toDirY, float toDirZ, Quaternionf dest) {
        return dest.rotationTo(x, y, z, toDirX, toDirY, toDirZ);
    }

    public Vector3f rotateAxis(float angle, float aX, float aY, float aZ, Vector3f dest) {
        if (aY == 0.0f && aZ == 0.0f && absEqualsOne(aX))
            return rotateX(aX * angle, dest);
        else if (aX == 0.0f && aZ == 0.0f && absEqualsOne(aY))
            return rotateY(aY * angle, dest);
        else if (aX == 0.0f && aY == 0.0f && absEqualsOne(aZ))
            return rotateZ(aZ * angle, dest);
        return rotateAxisInternal(angle, aX, aY, aZ, dest);
    }

    private Vector3f rotateAxisInternal(float angle, float aX, float aY, float aZ, Vector3f dest) {
        float hangle = angle * 0.5f;
        float sinAngle = Math.sin(hangle);
        float qx = aX * sinAngle, qy = aY * sinAngle, qz = aZ * sinAngle;
        float qw = Math.cosFromSin(sinAngle, hangle);
        float w2 = qw * qw, x2 = qx * qx, y2 = qy * qy, z2 = qz * qz, zw = qz * qw;
        float xy = qx * qy, xz = qx * qz, yw = qy * qw, yz = qy * qz, xw = qx * qw;
        float x = this.x, y = this.y, z = this.z;
        dest.x = (w2 + x2 - z2 - y2) * x + (-zw + xy - zw + xy) * y + (yw + xz + xz + yw) * z;
        dest.y = (xy + zw + zw + xy) * x + (y2 - z2 + w2 - x2) * y + (yz + yz - xw - xw) * z;
        dest.z = (xz - yw + xz - yw) * x + (yz + yz + xw + xw) * y + (z2 - y2 - x2 + w2) * z;
        return dest;
    }

    public Vector3f rotateX(float angle, Vector3f dest) {
        float sin = Math.sin(angle), cos = Math.cosFromSin(sin, angle);
        float y = this.y * cos - this.z * sin;
        float z = this.y * sin + this.z * cos;
        dest.x = this.x;
        dest.y = y;
        dest.z = z;
        return dest;
    }

    public Vector3f rotateY(float angle, Vector3f dest) {
        float sin = Math.sin(angle), cos = Math.cosFromSin(sin, angle);
        float x = this.x * cos + this.z * sin;
        float z = -this.x * sin + this.z * cos;
        dest.x = x;
        dest.y = this.y;
        dest.z = z;
        return dest;
    }

    public Vector3f rotateZ(float angle, Vector3f dest) {
        float sin = Math.sin(angle), cos = Math.cosFromSin(sin, angle);
        float x = this.x * cos - this.y * sin;
        float y = this.x * sin + this.y * cos;
        dest.x = x;
        dest.y = y;
        dest.z = this.z;
        return dest;
    }

    public float lengthSquared() {
        return Math.fma(x, x, Math.fma(y, y, z * z));
    }

    public float length() {
        return Math.sqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
    }

    public Vector3f normalize(Vector3f dest) {
        float scalar = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }

    public Vector3f normalize(float length, Vector3f dest) {
        float scalar = Math.invsqrt(Math.fma(x, x, Math.fma(y, y, z * z))) * length;
        dest.x = this.x * scalar;
        dest.y = this.y * scalar;
        dest.z = this.z * scalar;
        return dest;
    }

    public Vector3f cross(Vector3fc v, Vector3f dest) {
        float rx = Math.fma(y, v.z(), -z * v.y());
        float ry = Math.fma(z, v.x(), -x * v.z());
        float rz = Math.fma(x, v.y(), -y * v.x());
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }

    public Vector3f cross(float x, float y, float z, Vector3f dest) {
        float rx = Math.fma(this.y, z, -this.z * y);
        float ry = Math.fma(this.z, x, -this.x * z);
        float rz = Math.fma(this.x, y, -this.y * x);
        dest.x = rx;
        dest.y = ry;
        dest.z = rz;
        return dest;
    }

    public float distance(Vector3fc v) {
        float dx = this.x - v.x();
        float dy = this.y - v.y();
        float dz = this.z - v.z();
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }

    public float distance(float x, float y, float z) {
        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;
        return Math.sqrt(Math.fma(dx, dx, Math.fma(dy, dy, dz * dz)));
    }

    public float distanceSquared(Vector3fc v) {
        float dx = this.x - v.x();
        float dy = this.y - v.y();
        float dz = this.z - v.z();
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }

    public float distanceSquared(float x, float y, float z) {
        float dx = this.x - x;
        float dy = this.y - y;
        float dz = this.z - z;
        return Math.fma(dx, dx, Math.fma(dy, dy, dz * dz));
    }

    public float dot(Vector3fc v) {
        return Math.fma(this.x, v.x(), Math.fma(this.y, v.y(), this.z * v.z()));
    }

    public float dot(float x, float y, float z) {
        return Math.fma(this.x, x, Math.fma(this.y, y, this.z * z));
    }

    public float angleCos(Vector3fc v) {
        float x = this.x, y = this.y, z = this.z;
        float length1Squared = Math.fma(x, x, Math.fma(y, y, z * z));
        float length2Squared = Math.fma(v.x(), v.x(), Math.fma(v.y(), v.y(), v.z() * v.z()));
        float dot = Math.fma(x, v.x(), Math.fma(y, v.y(), z * v.z()));
        return dot / (float) Math.sqrt(length1Squared * length2Squared);
    }

    public float angle(Vector3fc v) {
        float cos = angleCos(v);
        // This is because sometimes cos goes above 1 or below -1 because of lost precision
        cos = cos < 1 ? cos : 1;
        cos = cos > -1 ? cos : -1;
        return Math.acos(cos);
    }

    public float angleSigned(Vector3fc v, Vector3fc n) {
        return angleSigned(v.x(), v.y(), v.z(), n.x(), n.y(), n.z());
    }

    public float angleSigned(float x, float y, float z, float nx, float ny, float nz) {
        float tx = this.x, ty = this.y, tz = this.z;
        return Math.atan2(
                (ty * z - tz * y) * nx + (tz * x - tx * z) * ny + (tx * y - ty * x) * nz,
                tx * x + ty * y + tz * z);
    }

    public Vector3f min(Vector3fc v, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = x < v.x() ? x : v.x();
        dest.y = y < v.y() ? y : v.y();
        dest.z = z < v.z() ? z : v.z();
        return dest;
    }

    public Vector3f max(Vector3fc v, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        dest.x = x > v.x() ? x : v.x();
        dest.y = y > v.y() ? y : v.y();
        dest.z = z > v.z() ? z : v.z();
        return dest;
    }

    public Vector3f negate(Vector3f dest) {
        dest.x = -x;
        dest.y = -y;
        dest.z = -z;
        return dest;
    }

    public Vector3f absolute(Vector3f dest) {
        dest.x = Math.abs(this.x);
        dest.y = Math.abs(this.y);
        dest.z = Math.abs(this.z);
        return dest;
    }

    public Vector3f reflect(Vector3fc normal, Vector3f dest) {
        return reflect(normal.x(), normal.y(), normal.z(), dest);
    }

    public Vector3f reflect(float x, float y, float z, Vector3f dest) {
        float dot = this.dot(x, y, z);
        dest.x = this.x - (dot + dot) * x;
        dest.y = this.y - (dot + dot) * y;
        dest.z = this.z - (dot + dot) * z;
        return dest;
    }

    public Vector3f half(Vector3fc other, Vector3f dest) {
        return half(other.x(), other.y(), other.z(), dest);
    }

    public Vector3f half(float x, float y, float z, Vector3f dest) {
        return dest.set(this).add(x, y, z).normalize();
    }

    public Vector3f smoothStep(Vector3fc v, float t, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        float t2 = t * t;
        float t3 = t2 * t;
        dest.x = (x + x - v.x() - v.x()) * t3 + (3.0f * v.x() - 3.0f * x) * t2 + x * t + x;
        dest.y = (y + y - v.y() - v.y()) * t3 + (3.0f * v.y() - 3.0f * y) * t2 + y * t + y;
        dest.z = (z + z - v.z() - v.z()) * t3 + (3.0f * v.z() - 3.0f * z) * t2 + z * t + z;
        return dest;
    }

    public Vector3f hermite(Vector3fc t0, Vector3fc v1, Vector3fc t1, float t, Vector3f dest) {
        float x = this.x, y = this.y, z = this.z;
        float t2 = t * t;
        float t3 = t2 * t;
        dest.x = (x + x - v1.x() - v1.x() + t1.x() + t0.x()) * t3 + (3.0f * v1.x() - 3.0f * x - t0.x() - t0.x() - t1.x()) * t2 + x * t + x;
        dest.y = (y + y - v1.y() - v1.y() + t1.y() + t0.y()) * t3 + (3.0f * v1.y() - 3.0f * y - t0.y() - t0.y() - t1.y()) * t2 + y * t + y;
        dest.z = (z + z - v1.z() - v1.z() + t1.z() + t0.z()) * t3 + (3.0f * v1.z() - 3.0f * z - t0.z() - t0.z() - t1.z()) * t2 + z * t + z;
        return dest;
    }

    public Vector3f lerp(Vector3fc other, float t, Vector3f dest) {
        dest.x = Math.fma(other.x() - x, t, x);
        dest.y = Math.fma(other.y() - y, t, y);
        dest.z = Math.fma(other.z() - z, t, z);
        return dest;
    }

    public float get(int component) throws IllegalArgumentException {
        switch (component) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Vector3i get(int mode, Vector3i dest) {
        dest.x = Math.roundUsing(this.x(), mode);
        dest.y = Math.roundUsing(this.y(), mode);
        dest.z = Math.roundUsing(this.z(), mode);
        return dest;
    }

    public Vector3f get(Vector3f dest) {
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        return dest;
    }

    public Vector3d get(Vector3d dest) {
        dest.x = this.x();
        dest.y = this.y();
        dest.z = this.z();
        return dest;
    }

    public int maxComponent() {
        float absX = Math.abs(x);
        float absY = Math.abs(y);
        float absZ = Math.abs(z);
        if (absX >= absY && absX >= absZ) {
            return 0;
        } else if (absY >= absZ) {
            return 1;
        }
        return 2;
    }

    public int minComponent() {
        float absX = Math.abs(x);
        float absY = Math.abs(y);
        float absZ = Math.abs(z);
        if (absX < absY && absX < absZ) {
            return 0;
        } else if (absY < absZ) {
            return 1;
        }
        return 2;
    }

    public Vector3f orthogonalize(Vector3fc v, Vector3f dest) {
        /*
         * http://lolengine.net/blog/2013/09/21/picking-orthogonal-vector-combing-coconuts
         */
        float rx, ry, rz;
        if (Math.abs(v.x()) > Math.abs(v.z())) {
            rx = -v.y();
            ry = v.x();
            rz = 0.0f;
        } else {
            rx = 0.0f;
            ry = -v.z();
            rz = v.y();
        }
        float invLen = Math.invsqrt(rx * rx + ry * ry + rz * rz);
        dest.x = rx * invLen;
        dest.y = ry * invLen;
        dest.z = rz * invLen;
        return dest;
    }

    public Vector3f orthogonalizeUnit(Vector3fc v, Vector3f dest) {
        return orthogonalize(v, dest);
    }

    public Vector3f floor(Vector3f dest) {
        dest.x = Math.floor(x);
        dest.y = Math.floor(y);
        dest.z = Math.floor(z);
        return dest;
    }

    public Vector3f ceil(Vector3f dest) {
        dest.x = Math.ceil(x);
        dest.y = Math.ceil(y);
        dest.z = Math.ceil(z);
        return dest;
    }

    public Vector3f round(Vector3f dest) {
        dest.x = Math.round(x);
        dest.y = Math.round(y);
        dest.z = Math.round(z);
        return dest;
    }

    public boolean isFinite() {
        return Math.isFinite(x) && Math.isFinite(y) && Math.isFinite(z);
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(x);
        result = prime * result + Float.floatToIntBits(y);
        result = prime * result + Float.floatToIntBits(z);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector3f other = (Vector3f) obj;
        if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
            return false;
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
            return false;
        if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
            return false;
        return true;
    }

    public boolean equals(Vector3fc v, float delta) {
        if (this == v)
            return true;
        if (v == null)
            return false;
        if (!Runtime.equals(x, v.x(), delta))
            return false;
        if (!Runtime.equals(y, v.y(), delta))
            return false;
        if (!Runtime.equals(z, v.z(), delta))
            return false;
        return true;
    }

    public boolean equals(float x, float y, float z) {
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(x))
            return false;
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(y))
            return false;
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(z))
            return false;
        return true;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    static boolean absEqualsOne(float r) {
        return (Float.floatToRawIntBits(r) & 0x7FFFFFFF) == 0x3F800000;
    }

    static boolean absEqualsOne(double r) {
        return (Double.doubleToRawLongBits(r) & 0x7FFFFFFFFFFFFFFFL) == 0x3FF0000000000000L;
    }
}

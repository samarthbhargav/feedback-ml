

def chk_type(obj, clazz):
    if not isinstance(obj, clazz):
        raise ValueError("'{}' is not of instance {}".format(obj, clazz))
